package ru.trickyfoxy.lab8.commands;

import ru.trickyfoxy.lab8.exceptions.NoUniqueId;
import ru.trickyfoxy.lab8.exceptions.NotFountId;
import ru.trickyfoxy.lab8.collection.RouteStorage;
import ru.trickyfoxy.lab8.utils.ReadWriteInterface;

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
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage, String username, Boolean[] updated) throws NotFountId, IOException, NoUniqueId, SQLException {
        route.validate();
        route.setCreator(username);
        Long id = Long.parseLong(argument);
        routeStorage.getDatabaseManager().updateById(id, route, username);
        routeStorage.update(id, route);
        updated[0] = Boolean.TRUE;
        readWriteInterface.writeln("Элемент обновлён");
        return null;
    }
}
