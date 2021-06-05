package ru.trickyfoxy.lab7.commands;

import ru.trickyfoxy.lab7.collection.RouteStorage;
import ru.trickyfoxy.lab7.utils.ReadWriteInterface;

import java.io.IOException;
import java.sql.SQLException;

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
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage, String username) throws IOException, SQLException {
        routeStorage.getDatabaseManager().removeGreater(route, username);
        routeStorage.removeGreater(route, username);
        return null;
    }
}
