package ru.trickyfoxy.lab5.commands;

import ru.trickyfoxy.lab5.collection.RouteStorage;
import ru.trickyfoxy.lab5.utils.ReadWriteInterface;

import java.io.IOException;

public class Clear extends Command {
    public Clear() {
        name = "clear";
        description = "очистить коллекцию";
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage) throws IOException {
        routeStorage.clear();
        readWriteInterface.writeln("Коллекция очищена");
        return null;
    }
}
