package ru.trickyfoxy.lab5.commands;

import ru.trickyfoxy.lab5.collection.RouteStorage;
import ru.trickyfoxy.lab5.exceptions.*;
import ru.trickyfoxy.lab5.utils.ReadWriteInterface;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Мененджер всех доступных команд
 */
public class CommandsManager {
    public static CommandsManager instance;

    private Map<String, Command> commands = new HashMap<>();

    public static CommandsManager getInstance() {
        if (instance == null) {
            instance = new CommandsManager();
        }
        return instance;
    }

    /**
     * Констуктор задающий доступные команды
     */
    private CommandsManager() {
        addCommand(new Add());
        addCommand(new AddIfMax());
        addCommand(new AddIfMin());
        addCommand(new Clear());
        addCommand(new CountGreaterThanDistance());
        addCommand(new ExecuteScript());
        addCommand(new Exit());
        addCommand(new Help());
        addCommand(new Info());
        addCommand(new MinByTo());
        addCommand(new RemoveAnyByDistance());
        addCommand(new RemoveAnyByDistance());
        addCommand(new RemoveById());
        addCommand(new RemoveGreater());
        addCommand(new Save());
        addCommand(new Show());
        addCommand(new Update());
    }

    /**
     * Добавляет команду в мененджер
     *
     * @param cmd команда для добавления в реестр
     */
    private void addCommand(Command cmd) {
        commands.put(cmd.getName(), cmd);
    }

    public List<Command> getAllCommands() {
        return commands.keySet().stream().map(x -> (commands.get(x))).collect(Collectors.toList());
    }

    /**
     * Элемент стека вызовов скриптов
     */
    private class StackLevel {
        ReadWriteInterface IO;
        String scriptFilePath;

        public StackLevel(ReadWriteInterface IO, String scriptFilePath) {
            this.IO = IO;
            this.scriptFilePath = scriptFilePath;
        }
    }

    /**
     * Стек вызовов скриптов
     */
    private Stack<StackLevel> scriptStack = new Stack<>();

    /**
     * список файлов в стека вызовов скриптов
     */
    private Set<String> listOfPathScript = new HashSet<>();

    /**
     * @return возврашает IO интерфейс для текущего скрипта
     */
    private ReadWriteInterface getCurrentIO() {
        return scriptStack.lastElement().IO;
    }

    /**
     * @return запрашивает следующую команду из текущего IO
     */
    private Command getNextCommand() throws InvalidCommand {
        String str = getCurrentIO().readString();
        if (str == null) {
            return null;
        }
        if (!str.isEmpty()) {
            Command cmd = commands.get(str);
            if (cmd == null) {
                throw new InvalidCommand("Неизвестная команда");
            }
            return cmd;
        }
        return null;
    }

    /**
     * Запускает цикл обработки команд от пользователя
     *
     * @param IO           IO интерфейс, через который происходит общение с пользователем
     * @param routeStorage Route Storage с которым мы работаем
     */
    public void loop(ReadWriteInterface IO, RouteStorage routeStorage) throws FileNotFoundException {
        scriptStack.push(new StackLevel(IO, IO.getName()));
        listOfPathScript.add(IO.getName());

        while (!scriptStack.isEmpty()) {
            Command cmd = null;
            try {
                cmd = getNextCommand();
            } catch (InvalidCommand invalidCommand) {
                try {
                    getCurrentIO().writeln(invalidCommand.getMessage());
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    break;
                }
                continue;
            }
            if (cmd == null) {
                popFromScriptStack();
                if (scriptStack.isEmpty()) {
                    break;
                }
                continue;
            }
            String scriptPath = null;
            try {
                scriptPath = cmd.execute(getCurrentIO(), routeStorage);
            } catch (ExitFromScriptException e) {
                popFromScriptStack();
                if(scriptStack.empty()){
                    return;
                }
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
                resetStack();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                break;
            } catch (NoUniqueId e) {
                System.err.println("Попытка добавить элемент с уже существующим id");
                resetStack();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (NotFountId e) {
                System.err.println(e.getMessage());
                resetStack();
            } catch (InvalidFieldException e){
                System.err.println(e.getMessage());
                resetStack();
            }
            if (scriptPath != null) {
                Path path = Paths.get(scriptPath);
                ReadWriteInterface newIO = new ReadWriteInterface(new FileReader(path.toFile()), IO.getWriter(), false);
                String absolutePath = path.toAbsolutePath().toString();
                scriptStack.push(new StackLevel(newIO, absolutePath));
                if (listOfPathScript.contains(absolutePath)) {
                    StringBuilder trace = new StringBuilder();
                    int lvl = 0;
                    while (scriptStack.size() > 1) {
                        for (int i = 0; i < lvl; i++) {
                            trace.append(">");
                        }
                        trace.append(scriptStack.lastElement().scriptFilePath).append("\n");
                        lvl++;
                        listOfPathScript.remove(scriptStack.lastElement().scriptFilePath);
                        scriptStack.pop();
                    }
                    System.err.println("Найдена рекурсия в запуске команды execute_script");
                    System.err.println(trace.toString());
                    resetStack();
                }
                listOfPathScript.add(absolutePath);
            }
        }

    }

    private void popFromScriptStack() {
        listOfPathScript.remove(getCurrentIO().getName());
        scriptStack.pop();
    }

    private void resetStack() {
        while (scriptStack.size() > 1) {
            listOfPathScript.remove(scriptStack.lastElement().scriptFilePath);
            scriptStack.pop();
        }
    }

}
