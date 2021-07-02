package ru.trickyfoxy.lab8.commands;

import ru.trickyfoxy.lab8.collection.RouteStorage;
import ru.trickyfoxy.lab8.utils.ReadWriteInterface;

import java.io.IOException;
import java.sql.SQLException;

public class AddIfMax extends Command {
    public AddIfMax() {
        name = "add_id_max";
        description = "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции";
    }

    @Override
    public void fillArgument(ReadWriteInterface readWriteInterface) throws IOException {
        route = readWriteInterface.readRoute();
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage, String username, Boolean[] updated) throws IOException, SQLException {
        route.validate();
        route.setCreator(username);
        Double mx = routeStorage.getDatabaseManager().getMaxByDistance(username);
        if (mx < route.getDistance()) {
            routeStorage.getDatabaseManager().insertToDB(route, username);
            routeStorage.add_if_max(route);
            updated[0] = Boolean.TRUE;
            readWriteInterface.writeln("Элемент добавлен");
        } else {
            readWriteInterface.writeln("Элемент не добавлен");
        }
        return null;
    }
}
