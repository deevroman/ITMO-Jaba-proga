package ru.trickyfoxy.lab8.commands;

import ru.trickyfoxy.lab8.collection.RouteStorage;
import ru.trickyfoxy.lab8.utils.ReadWriteInterface;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class Info extends Command {
    public Info() {
        name = "info";
        description = "выводит в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }

    @Override
    public void fillArgument(ReadWriteInterface readWriteInterface) {
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage, String username, Boolean[] updated) throws IOException {
        String sb = new SimpleDateFormat("yyyy-MM-dd").format(routeStorage.getCreationDate()) + "\n" +
                routeStorage.size() + "\n" +
                routeStorage.getCollectionClass().getName();
        readWriteInterface.writeln(sb);
        return null;
        // TODO IDEA bug? Rename bad val broke name
    }
}
