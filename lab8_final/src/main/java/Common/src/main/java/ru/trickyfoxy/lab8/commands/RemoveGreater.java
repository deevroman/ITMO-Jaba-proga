package ru.trickyfoxy.lab8.commands;

import ru.trickyfoxy.lab8.collection.Route;
import ru.trickyfoxy.lab8.collection.RouteStorage;
import ru.trickyfoxy.lab8.utils.ReadWriteInterface;

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
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage, String username, Boolean[] updated) throws IOException, SQLException {
        routeStorage.getDatabaseManager().removeGreater(Float.parseFloat(argument), username);
        route = new Route();
        route.setDistance(Float.parseFloat(argument));
        routeStorage.removeGreater(route, username);
        updated[0] = Boolean.TRUE;
        return null;
    }
}
