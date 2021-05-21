package ru.trickyfoxy.lab6.commands;

import ru.trickyfoxy.lab6.collection.RouteStorage;
import ru.trickyfoxy.lab6.utils.ReadWriteInterface;

import java.io.IOException;

public class CountGreaterThanDistance extends Command {
    public CountGreaterThanDistance() {
        name = "count_greater_than_distance";
        description = "вывести количество элементов, значение поля distance которых больше заданного";
    }

    @Override
    public void fillArgument(ReadWriteInterface readWriteInterface) {
        argument = String.valueOf(Float.parseFloat(readWriteInterface.readString(true)));
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage) throws IOException {
        float distance = Float.parseFloat(argument);
        readWriteInterface.writeln(String.valueOf(routeStorage.CountLessThanDistance(distance)));
        return null;
    }
}
