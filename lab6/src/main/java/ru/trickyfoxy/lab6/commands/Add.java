package ru.trickyfoxy.lab6.commands;

import ru.trickyfoxy.lab6.collection.RouteStorage;
import ru.trickyfoxy.lab6.utils.ReadWriteInterface;

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
    public void fillArgument(ReadWriteInterface readWriteInterface) throws IOException {
        route = readWriteInterface.readRoute();
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage) throws IOException {
        routeStorage.addAndGenId(route);
        readWriteInterface.writeln("Элемент добавлен");
        return null;
    }
}
