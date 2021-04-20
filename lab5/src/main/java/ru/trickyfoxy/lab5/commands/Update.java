package ru.trickyfoxy.lab5.commands;

import ru.trickyfoxy.lab5.collection.Route;
import ru.trickyfoxy.lab5.collection.RouteStorage;
import ru.trickyfoxy.lab5.exceptions.NoUniqueId;
import ru.trickyfoxy.lab5.exceptions.NotFountId;
import ru.trickyfoxy.lab5.utils.ReadWriteInterface;

import java.io.IOException;
import java.util.Date;

public class Update extends Command {
    public Update() {
        name = "update";
        description = "обновить значение элемента коллекции, id которого равен заданному";
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage) throws NotFountId, IOException, NoUniqueId {
        Long id = Long.parseLong(readWriteInterface.readString(true));
        if (!routeStorage.containsId(id)){
            throw new NotFountId("Такого id нет в коллекции");
        }
        Route route = readWriteInterface.readRoute();
        route.setId(id);
        route.setCreationDate(new Date());
        routeStorage.removeById(id);
        routeStorage.add(route);
        readWriteInterface.writeln("Элемент обновлён");
        return null;
    }
}
