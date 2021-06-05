package ru.trickyfoxy.lab7.commands;

import ru.trickyfoxy.lab7.exceptions.NoUniqueId;
import ru.trickyfoxy.lab7.exceptions.NotFountId;
import ru.trickyfoxy.lab7.collection.RouteStorage;
import ru.trickyfoxy.lab7.utils.ReadWriteInterface;

import java.io.IOException;
import java.sql.SQLException;

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
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage, String username) throws NotFountId, IOException, NoUniqueId, SQLException {
        Long id = Long.parseLong(argument);
        routeStorage.getDatabaseManager().updateById(id, route, username);
        routeStorage.update(id, route);
        readWriteInterface.writeln("Элемент обновлён");
        return null;
    }
}
