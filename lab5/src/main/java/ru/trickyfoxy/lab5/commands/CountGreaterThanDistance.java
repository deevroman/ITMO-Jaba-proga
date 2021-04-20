package ru.trickyfoxy.lab5.commands;

import ru.trickyfoxy.lab5.collection.RouteStorage;
import ru.trickyfoxy.lab5.utils.ReadWriteInterface;

import java.io.IOException;

public class CountGreaterThanDistance extends Command {
    public CountGreaterThanDistance() {
        name = "count_greater_than_distance";
        description = "вывести количество элементов, значение поля distance которых больше заданного";
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage) throws IOException {
        float distance = Float.parseFloat(readWriteInterface.readString(true));
        readWriteInterface.writeln(String.valueOf(routeStorage.CountLessThanDistance(distance)));
        return null;
    }
}
