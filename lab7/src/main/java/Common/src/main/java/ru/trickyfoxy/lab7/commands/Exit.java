package ru.trickyfoxy.lab7.commands;

import ru.trickyfoxy.lab7.exceptions.ExitFromScriptException;
import ru.trickyfoxy.lab7.collection.RouteStorage;
import ru.trickyfoxy.lab7.utils.ReadWriteInterface;

public class Exit extends Command {
    public Exit() {
        name = "exit";
        description = "Завершает программу (без сохранения в файл)";
    }

    @Override
    public void fillArgument(ReadWriteInterface readWriteInterface) {
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage, String username) {
        throw new ExitFromScriptException();
    }
}
