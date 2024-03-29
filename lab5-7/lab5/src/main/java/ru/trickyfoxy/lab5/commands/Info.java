package ru.trickyfoxy.lab5.commands;

import ru.trickyfoxy.lab5.collection.RouteStorage;
import ru.trickyfoxy.lab5.utils.ReadWriteInterface;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class Info extends Command {
    public Info() {
        name = "info";
        description = "выводит в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage) throws IOException {
        String sb = "Дата инициализации коллекции: " + new SimpleDateFormat("yyyy-MM-dd").format(routeStorage.getCreationDate()) + "\n" +
                "Количество элементов в коллекции: " + routeStorage.size() + "\n" +
                "Тип коллекции: " + routeStorage.getCollectionClass().getName();
        readWriteInterface.writeln(sb);
        return null;
        // TODO IDEA bug? Rename bad val broke name
    }
}
