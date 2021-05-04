package ru.trickyfoxy.lab6.commands;

import ru.trickyfoxy.lab6.collection.Route;
import ru.trickyfoxy.lab6.collection.RouteStorage;
import ru.trickyfoxy.lab6.exceptions.InvalidFieldException;
import ru.trickyfoxy.lab6.exceptions.NoUniqueId;
import ru.trickyfoxy.lab6.exceptions.NotFountId;
import ru.trickyfoxy.lab6.utils.ReadWriteInterface;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

abstract public class Command implements Serializable {
    protected String name;
    protected Route route;
    protected String argument;

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

    public abstract void fillArgument(ReadWriteInterface readWriteInterface) throws IOException;

    public abstract String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage) throws FileNotFoundException, IOException, NotFountId, NoUniqueId, ParserConfigurationException, InvalidFieldException;

    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", route=" + route +
                ", argument='" + argument + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
