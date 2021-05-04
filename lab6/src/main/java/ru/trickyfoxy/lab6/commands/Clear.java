package ru.trickyfoxy.lab6.commands;

import ru.trickyfoxy.lab6.collection.RouteStorage;
import ru.trickyfoxy.lab6.utils.ReadWriteInterface;

import java.io.IOException;

public class Clear extends Command {
    public Clear() {
        name = "clear";
        description = "очистить коллекцию";
    }

    @Override
    public void fillArgument(ReadWriteInterface readWriteInterface) {
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage) throws IOException {
        routeStorage.clear();
        readWriteInterface.writeln("Коллекция очищена");
        return null;
    }
}
