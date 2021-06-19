package ru.trickyfoxy.lab6.commands;

import ru.trickyfoxy.lab6.collection.RouteStorage;
import ru.trickyfoxy.lab6.utils.ReadWriteInterface;

import java.io.IOException;

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
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage) throws IOException {
        if (routeStorage.add_if_max(route)) {
            readWriteInterface.writeln("Элемент добавлен");
        } else {
            readWriteInterface.writeln("Элемент не добавлен");
        }
        return null;
    }
}
