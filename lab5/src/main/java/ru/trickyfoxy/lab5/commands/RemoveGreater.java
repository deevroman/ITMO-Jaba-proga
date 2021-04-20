package ru.trickyfoxy.lab5.commands;

import ru.trickyfoxy.lab5.collection.Route;
import ru.trickyfoxy.lab5.collection.RouteStorage;
import ru.trickyfoxy.lab5.utils.ReadWriteInterface;

import java.io.IOException;

public class RemoveGreater extends Command {
    public RemoveGreater() {
        name = "remove_greater";
        description = "удалить из коллекции все элементы, превышающие заданный";
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage) throws IOException {
        Route route = readWriteInterface.readRoute();
        routeStorage.removeGreater(route);
        return null;
    }
}
