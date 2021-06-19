package ru.trickyfoxy.lab5.commands;

import ru.trickyfoxy.lab5.collection.RouteStorage;
import ru.trickyfoxy.lab5.exceptions.InvalidFieldException;
import ru.trickyfoxy.lab5.exceptions.NoUniqueId;
import ru.trickyfoxy.lab5.exceptions.NotFountId;
import ru.trickyfoxy.lab5.utils.ReadWriteInterface;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;

abstract public class Command {
    protected String name;

    protected Command(String name) {
        this.name = name;
    }

    protected Command() {
    }

    /**
     * @return имя команды
     */
    public String getName() {
        return name;
    }


    protected String description;

    /**
     * @return описание команды
     */
    public String getDescription() {
        return description;
    }

    public abstract String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage) throws FileNotFoundException, IOException, NotFountId, NoUniqueId, ParserConfigurationException, InvalidFieldException;


}
