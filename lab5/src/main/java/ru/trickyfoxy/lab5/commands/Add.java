package ru.trickyfoxy.lab5.commands;

import ru.trickyfoxy.lab5.collection.Route;
import ru.trickyfoxy.lab5.collection.RouteStorage;
import ru.trickyfoxy.lab5.utils.ReadWriteInterface;

import java.io.IOException;

/**
 * Команда add
 */
public class Add extends Command {
    public Add() {
        name = "add";
        description = "добавить новый элемент в коллекцию";
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage) throws IOException {
        Route route = readWriteInterface.readRoute();
        routeStorage.addAndGenId(route);
        readWriteInterface.writeln("Элемент добавлен");
        return null;
    }
}
