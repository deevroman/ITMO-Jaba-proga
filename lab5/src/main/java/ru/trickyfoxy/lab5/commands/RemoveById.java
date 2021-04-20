package ru.trickyfoxy.lab5.commands;

import ru.trickyfoxy.lab5.collection.RouteStorage;
import ru.trickyfoxy.lab5.exceptions.InvalidFieldException;
import ru.trickyfoxy.lab5.utils.ReadWriteInterface;

import java.io.IOException;

public class RemoveById extends Command {
    public RemoveById() {
        name = "remove_by_id";
        description = "удалить элемент из коллекции по его id";
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage) throws InvalidFieldException, IOException {
        Long id = Long.parseLong(readWriteInterface.readString(true));
        if (!routeStorage.removeById(id)) {
            throw new InvalidFieldException("Данного id нет в коллекции");
        } else {
            readWriteInterface.writeln("Элемент удалён");
        }
        return null;
    }
}
