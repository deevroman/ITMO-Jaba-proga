package ru.trickyfoxy.lab8.commands;

import ru.trickyfoxy.lab8.collection.RouteStorage;
import ru.trickyfoxy.lab8.utils.ReadWriteInterface;

import java.io.IOException;

public class Show extends Command {
    public Show() {
        name = "show";
        description = "вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }

    @Override
    public void fillArgument(ReadWriteInterface readWriteInterface) {
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage, String username) throws IOException {
        String out = routeStorage.show();
        if (out.equals("")) {
            readWriteInterface.writeln("");
        } else {
            readWriteInterface.write(routeStorage.showSorted());
        }
        return null;
    }
}
