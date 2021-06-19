package ru.trickyfoxy.lab8.commands;

import ru.trickyfoxy.lab8.collection.RouteStorage;
import ru.trickyfoxy.lab8.utils.ReadWriteInterface;

import java.io.IOException;

public class MinByTo extends Command {
    public MinByTo() {
        name = "min_by_to";
        description = "вывести любой объект из коллекции, значение поля to которого является минимальным";
    }

    @Override
    public void fillArgument(ReadWriteInterface readWriteInterface) {
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage, String username) throws IOException {
        if (routeStorage.size() == 0) {
            return null;
        }
        readWriteInterface.writeln(routeStorage.min_by_to().toString());
        return null;
    }
}
