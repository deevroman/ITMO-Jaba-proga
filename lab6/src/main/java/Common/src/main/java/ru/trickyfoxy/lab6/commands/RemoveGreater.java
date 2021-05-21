package ru.trickyfoxy.lab6.commands;

import ru.trickyfoxy.lab6.collection.RouteStorage;
import ru.trickyfoxy.lab6.utils.ReadWriteInterface;

import java.io.IOException;

public class RemoveGreater extends Command {
    public RemoveGreater() {
        name = "remove_greater";
        description = "удалить из коллекции все элементы, превышающие заданный";
    }

    @Override
    public void fillArgument(ReadWriteInterface readWriteInterface) throws IOException {
        route = readWriteInterface.readRoute();
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage) throws IOException {
        routeStorage.removeGreater(route);
        return null;
    }
}
