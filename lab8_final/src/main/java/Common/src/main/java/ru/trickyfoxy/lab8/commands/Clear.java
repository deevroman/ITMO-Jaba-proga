package ru.trickyfoxy.lab8.commands;

import ru.trickyfoxy.lab8.collection.RouteStorage;
import ru.trickyfoxy.lab8.utils.ReadWriteInterface;

import java.io.IOException;
import java.sql.SQLException;

public class Clear extends Command {
    public Clear() {
        name = "clear";
        description = "очистить коллекцию";
    }

    @Override
    public void fillArgument(ReadWriteInterface readWriteInterface) {
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage, String username, Boolean[] updated) throws IOException, SQLException {
        routeStorage.getDatabaseManager().clear(username);
        routeStorage.clear(username);
        updated[0] = Boolean.TRUE;
        readWriteInterface.writeln("Созданные вами элементы очищены");
        return null;
    }
}
