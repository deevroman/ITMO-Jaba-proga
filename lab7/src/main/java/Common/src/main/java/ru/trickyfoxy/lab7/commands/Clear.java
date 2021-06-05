package ru.trickyfoxy.lab7.commands;

import ru.trickyfoxy.lab7.collection.RouteStorage;
import ru.trickyfoxy.lab7.utils.ReadWriteInterface;

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
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage, String username) throws IOException, SQLException {
        routeStorage.getDatabaseManager().clear(username);
        routeStorage.clear(username);
        readWriteInterface.writeln("Созданные вами элементы очищены");
        return null;
    }
}
