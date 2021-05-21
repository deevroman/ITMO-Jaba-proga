package ru.trickyfoxy.lab6.commands;

import ru.trickyfoxy.lab6.exceptions.ExitFromScriptException;
import ru.trickyfoxy.lab6.collection.RouteStorage;
import ru.trickyfoxy.lab6.utils.ReadWriteInterface;

public class Exit extends Command {
    public Exit() {
        name = "exit";
        description = "Завершает программу (без сохранения в файл)";
    }

    @Override
    public void fillArgument(ReadWriteInterface readWriteInterface) {
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage) {
        throw new ExitFromScriptException();
    }
}
