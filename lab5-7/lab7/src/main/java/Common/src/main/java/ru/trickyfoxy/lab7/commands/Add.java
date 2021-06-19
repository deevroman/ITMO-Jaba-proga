package ru.trickyfoxy.lab7.commands;

import ru.trickyfoxy.lab7.collection.RouteStorage;
import ru.trickyfoxy.lab7.utils.ReadWriteInterface;

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
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage, String username) throws IOException, SQLException {
        Long id = routeStorage.getDatabaseManager().insertToDB(route, username);
        route.setId(id);
        routeStorage.add(route);
        readWriteInterface.writeln("Элемент добавлен");
        return null;
    }
}
