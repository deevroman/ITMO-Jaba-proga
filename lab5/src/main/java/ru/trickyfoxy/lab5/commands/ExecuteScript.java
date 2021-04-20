package ru.trickyfoxy.lab5.commands;

import ru.trickyfoxy.lab5.collection.RouteStorage;
import ru.trickyfoxy.lab5.utils.ReadWriteInterface;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ExecuteScript extends Command {
    public ExecuteScript() {
        name = "execute_script";
        description = "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.";
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage) throws FileNotFoundException {
        String path = readWriteInterface.readString(true);
        if (!Files.exists(Paths.get(path))) {
            throw new FileNotFoundException("Файл " + path + " не найден");
        }
        return path;
    }
}
