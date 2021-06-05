package ru.trickyfoxy.lab7.commands;

import ru.trickyfoxy.lab7.collection.RouteStorage;
import ru.trickyfoxy.lab7.utils.ReadWriteInterface;

import java.io.IOException;
import java.sql.SQLException;

public class AddIfMin extends Command {
    public AddIfMin() {
        name = "add_if_min";
        description = "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции";
    }

    @Override
    public void fillArgument(ReadWriteInterface readWriteInterface) throws IOException {
        route = readWriteInterface.readRoute();
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage, String username) throws IOException, SQLException {
        Double mn = routeStorage.getDatabaseManager().getMinByDistance(username);
        if (mn > route.getDistance() && routeStorage.add_if_max(route)) {
            readWriteInterface.writeln("Элемент добавлен");
        } else {
            readWriteInterface.writeln("Элемент не добавлен");
        }
        return null;
    }
}
