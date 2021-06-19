package ru.trickyfoxy.lab8.commands;

import ru.trickyfoxy.lab8.collection.RouteStorage;
import ru.trickyfoxy.lab8.exceptions.InvalidFieldException;
import ru.trickyfoxy.lab8.utils.ReadWriteInterface;

import java.io.IOException;
import java.sql.SQLException;

public class RemoveById extends Command {
    public RemoveById() {
        name = "remove_by_id";
        description = "удалить элемент из коллекции по его id";
    }

    @Override
    public void fillArgument(ReadWriteInterface readWriteInterface) {
        argument = String.valueOf(Long.parseLong(readWriteInterface.readString(true)));
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage, String username) throws InvalidFieldException, IOException, SQLException {
        Long id = Long.parseLong(argument);
        if (routeStorage.getDatabaseManager().removeById(id, username)
                && !routeStorage.removeById(id)) {
            throw new InvalidFieldException("Данного id нет в коллекции");
        } else {
            readWriteInterface.writeln("Элемент удалён");
        }
        return null;
    }
}
