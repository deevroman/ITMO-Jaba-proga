package ru.trickyfoxy.lab8.commands;

import ru.trickyfoxy.lab8.collection.RouteStorage;
import ru.trickyfoxy.lab8.exceptions.InvalidFieldException;
import ru.trickyfoxy.lab8.exceptions.NoUniqueId;
import ru.trickyfoxy.lab8.exceptions.NotFountId;
import ru.trickyfoxy.lab8.utils.ReadWriteInterface;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Hello extends Command {
    public Hello() {
        name = "hello";
        description = "стартовая команда";
    }
    @Override
    public void fillArgument(ReadWriteInterface readWriteInterface) throws IOException {
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage, String username, Boolean[] updated) throws IOException, NotFountId, NoUniqueId, ParserConfigurationException, InvalidFieldException {
        readWriteInterface.writeln("Соединение установлено.");
        return null;
    }

}
