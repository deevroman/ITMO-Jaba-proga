package ru.trickyfoxy.lab7.commands;

import ru.trickyfoxy.lab7.exceptions.*;
import ru.trickyfoxy.lab7.collection.RouteStorageImpl;
import ru.trickyfoxy.lab7.utils.ReadWriteInterface;
import ru.trickyfoxy.lab7.utils.Connector;
import ru.trickyfoxy.lab7.utils.ServerAnswer;
import ru.trickyfoxy.lab7.utils.ServerAnswerStatus;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Менеджер всех доступных команд
 */
public class CommandsManager {
    public static CommandsManager instance;

    private final Map<String, Command> commands = new HashMap<>();

    public static CommandsManager getInstance() {
        if (instance == null) {
            instance = new CommandsManager();
        }
        return instance;
    }

    /**
     * Конструктор задающий доступные команды
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
        addCommand(new Show());
        addCommand(new Update());
    }

    /**
     * Добавляет команду в менеджер
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
    private final Stack<StackLevel> scriptStack = new Stack<>();

    /**
     * список файлов в стека вызовов скриптов
     */
    private final Set<String> listOfPathScript = new HashSet<>();

    /**
     * @return возвращает IO интерфейс для текущего скрипта
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
     * @param IO IO интерфейс, через который происходит общение с пользователем
     */
    public void loop(ReadWriteInterface IO, Connector connector) throws IOException, TimeoutConnectionException {
        scriptStack.push(new StackLevel(IO, IO.getName()));
        listOfPathScript.add(IO.getName());

        while (!scriptStack.isEmpty()) {
            Command cmd;
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
                cmd.fillArgument(getCurrentIO());
                if (cmd.name.equals("execute_script")) {
                    scriptPath = cmd.execute(new ReadWriteInterface(new InputStreamReader(System.in),
                            new OutputStreamWriter(System.out),
                            true), new RouteStorageImpl(), "");
                } else {
                    connector.reconnect();
                    if (cmd.name.equals("exit")) {
                        connector.sendCommand(new Exit());
                        throw new ExitFromScriptException();
                    } else {
                        connector.sendCommand(cmd);
                    }
                    ServerAnswer serverAnswer = (ServerAnswer) connector.receiver();
                    if (serverAnswer.status == ServerAnswerStatus.AUTH_ERROR) {
                        throw new TimeoutConnectionException(serverAnswer.message);
                    }
                    if (serverAnswer.status == ServerAnswerStatus.SERVER_ERROR) {
                        System.err.println(serverAnswer);
                    }
                    if (serverAnswer.status == ServerAnswerStatus.CLIENT_ERROR) {
                        System.err.println("Ошибка: ");
                    }
                    String result = serverAnswer.message;

                    getCurrentIO().writeln(result);
                    connector.disconnect();
                    if (cmd.name.equals("exit")) {
                        throw new ExitFromScriptException();
                    }
                }
            } catch (ExitFromScriptException e) {
                popFromScriptStack();
                if (scriptStack.empty()) {
                    throw new ExitFromScriptException();
                }
            } catch (FileNotFoundException | InvalidFieldException e) {
                System.err.println(e.getMessage());
                resetStack();
            } catch (NoUniqueId e) {
                System.err.println("Попытка добавить элемент с уже существующим id");
                resetStack();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (NotFountId notFountId) {
                notFountId.printStackTrace();
            } catch (TimeoutConnectionException e) {
                System.err.println("Сессия устарела. Необходимо авторизоваться снова");
                connector.setSession(null);
                throw e;
            } catch (SQLException throwables) {
                System.err.println("Беды с бдшкой");
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
                    System.err.println(trace);
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
