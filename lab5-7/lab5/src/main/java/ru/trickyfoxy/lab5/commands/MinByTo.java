package ru.trickyfoxy.lab5.commands;

import ru.trickyfoxy.lab5.collection.RouteStorage;
import ru.trickyfoxy.lab5.utils.ReadWriteInterface;

import java.io.IOException;

public class MinByTo extends Command {
    public MinByTo() {
        name = "min_by_to";
        description = "вывести любой объект из коллекции, значение поля to которого является минимальным";
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage) throws IOException {
        if(routeStorage.size() == 0){
            return null;
        }
        readWriteInterface.writeln(routeStorage.min_by_to().toString());
        return null;
    }
}
