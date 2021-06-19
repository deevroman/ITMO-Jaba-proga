package ru.trickyfoxy.lab8.commands;

import ru.trickyfoxy.lab8.collection.RouteStorage;
import ru.trickyfoxy.lab8.utils.ReadWriteInterface;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Save extends Command {
    public Save() {
        name = "save";
        description = "сохранить коллекцию в файл";
    }

    @Override
    public void fillArgument(ReadWriteInterface readWriteInterface) {
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage, String username) throws IOException, ParserConfigurationException {
        try {
            routeStorage.save();
        } catch (JAXBException e) {
            throw new IOException("Не удалось сохранить коллекцию в файл");
        }
        readWriteInterface.writeln("Коллекция сохранена");
        return null;
    }
}
