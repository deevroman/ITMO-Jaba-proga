package ru.trickyfoxy.lab8.commands;

import ru.trickyfoxy.lab8.collection.RouteStorage;
import ru.trickyfoxy.lab8.utils.ReadWriteInterface;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Команда add
 */
public class Add extends Command {
    public Add() {
        name = "add";
        description = "добавить новый элемент в коллекцию";
    }

    @Override
    public void fillArgument(ReadWriteInterface readWriteInterface) throws IOException {
        route = readWriteInterface.readRoute();
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage, String username, Boolean[] updated) throws IOException, SQLException {
        route.validate();
        route.setCreator(username);
        Long id = routeStorage.getDatabaseManager().insertToDB(route, username);
        route.setId(id);
        routeStorage.add(route);
        updated[0] = Boolean.TRUE;
        readWriteInterface.writeln("Элемент добавлен");
        return null;
    }
}
