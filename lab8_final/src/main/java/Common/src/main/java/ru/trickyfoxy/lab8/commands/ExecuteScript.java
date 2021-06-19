package ru.trickyfoxy.lab8.commands;

import ru.trickyfoxy.lab8.collection.RouteStorage;
import ru.trickyfoxy.lab8.utils.ReadWriteInterface;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ExecuteScript extends Command {
    public ExecuteScript() {
        name = "execute_script";
        description = "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.";
    }

    @Override
    public void fillArgument(ReadWriteInterface readWriteInterface) throws IOException {
        argument = readWriteInterface.readString(true);
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage, String username) throws FileNotFoundException {
        String path = argument;
        if (!Files.exists(Paths.get(path))) {
            throw new FileNotFoundException("Файл " + path + " не найден");
        }
        return path;
    }
}
