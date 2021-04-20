package ru.trickyfoxy.lab5.commands;

import ru.trickyfoxy.lab5.collection.RouteStorage;
import ru.trickyfoxy.lab5.exceptions.ExitFromScriptException;
import ru.trickyfoxy.lab5.utils.ReadWriteInterface;

public class Exit extends Command {
    public Exit() {
        name = "exit";
        description = "Завершает программу (без сохранения в файл)";
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage) {
        throw new ExitFromScriptException();
    }
}
