package ru.trickyfoxy.lab5.commands;

import ru.trickyfoxy.lab5.collection.Route;
import ru.trickyfoxy.lab5.collection.RouteStorage;
import ru.trickyfoxy.lab5.utils.ReadWriteInterface;

import java.io.IOException;

public class AddIfMin extends Command {
    public AddIfMin() {
        name = "add_if_min";
        description = "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции";
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage) throws IOException {
        Route route = readWriteInterface.readRoute();
        if (routeStorage.add_if_min(route)) {
            readWriteInterface.writeln("Элемент добавлен");
        } else {
            readWriteInterface.writeln("Элемент не добавлен");
        }
        return null;
    }
}
