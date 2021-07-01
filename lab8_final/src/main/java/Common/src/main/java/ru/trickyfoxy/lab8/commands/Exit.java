package ru.trickyfoxy.lab8.commands;

import ru.trickyfoxy.lab8.exceptions.ExitFromScriptException;
import ru.trickyfoxy.lab8.collection.RouteStorage;
import ru.trickyfoxy.lab8.utils.ReadWriteInterface;

public class Exit extends Command {
    public Exit() {
        name = "exit";
        description = "Завершает программу (без сохранения в файл)";
    }

    @Override
    public void fillArgument(ReadWriteInterface readWriteInterface) {
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage, String username, Boolean[] updated) {
        throw new ExitFromScriptException();
    }
}
