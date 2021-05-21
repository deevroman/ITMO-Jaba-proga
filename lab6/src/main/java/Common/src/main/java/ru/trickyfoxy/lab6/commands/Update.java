package ru.trickyfoxy.lab6.commands;

import ru.trickyfoxy.lab6.exceptions.NoUniqueId;
import ru.trickyfoxy.lab6.exceptions.NotFountId;
import ru.trickyfoxy.lab6.collection.RouteStorage;
import ru.trickyfoxy.lab6.utils.ReadWriteInterface;

import java.io.IOException;

public class Update extends Command {
    public Update() {
        name = "update";
        description = "обновить значение элемента коллекции, id которого равен заданному";
    }

    @Override
    public void fillArgument(ReadWriteInterface readWriteInterface) throws IOException {
        argument = String.valueOf(Long.parseLong(readWriteInterface.readString(true)));
        route = readWriteInterface.readRoute();
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage) throws NotFountId, IOException, NoUniqueId {
        Long id = Long.parseLong(argument);
        routeStorage.update(id, route);
        readWriteInterface.writeln("Элемент обновлён");
        return null;
    }
}
