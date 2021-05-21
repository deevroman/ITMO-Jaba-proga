package ru.trickyfoxy.lab6.commands;

import ru.trickyfoxy.lab6.collection.RouteStorage;
import ru.trickyfoxy.lab6.exceptions.InvalidFieldException;
import ru.trickyfoxy.lab6.exceptions.NoUniqueId;
import ru.trickyfoxy.lab6.exceptions.NotFountId;
import ru.trickyfoxy.lab6.utils.ReadWriteInterface;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Hello extends Command{
    public Hello() {
        name = "hello";
        description = "стартовая команда";
    }
    @Override
    public void fillArgument(ReadWriteInterface readWriteInterface) throws IOException {
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage) throws FileNotFoundException, IOException, NotFountId, NoUniqueId, ParserConfigurationException, InvalidFieldException {
        readWriteInterface.writeln("Соединение установлено.");
        return null;
    }

}
