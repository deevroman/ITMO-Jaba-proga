package ru.trickyfoxy.lab8.commands;

import ru.trickyfoxy.lab8.collection.RouteStorage;
import ru.trickyfoxy.lab8.utils.ReadWriteInterface;

import java.io.IOException;
import java.sql.SQLException;

public class RemoveAnyByDistance extends Command {
    public RemoveAnyByDistance() {
        name = "remove_any_by_distance";
        description = "удалить из коллекции один элемент, значение поля distance которого эквивалентно заданному";
    }

    @Override
    public void fillArgument(ReadWriteInterface readWriteInterface) {
        argument = String.valueOf(Float.parseFloat(readWriteInterface.readString(true)));
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage, String username) throws IOException, SQLException {
        float distance = Float.parseFloat(argument);
        if (routeStorage.getDatabaseManager().removeByDistance(distance, username)
            && routeStorage.remove_any_by_distance(distance)) {
            readWriteInterface.writeln("Элемент с таким distance найден и удалён");
        } else {
            readWriteInterface.writeln("Элемент с таким distance не найден");
        }
        return null;
    }
}
