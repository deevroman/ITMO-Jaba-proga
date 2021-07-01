package ru.trickyfoxy.lab8.commands;

import ru.trickyfoxy.lab8.collection.Route;
import ru.trickyfoxy.lab8.collection.RouteStorage;
import ru.trickyfoxy.lab8.exceptions.InvalidFieldException;
import ru.trickyfoxy.lab8.exceptions.NoUniqueId;
import ru.trickyfoxy.lab8.exceptions.NotFountId;
import ru.trickyfoxy.lab8.utils.ReadWriteInterface;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

public abstract class Command implements Serializable {
    protected String name;

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    protected Route route;

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

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

    /**
     * @param readWriteInterface
     * @param routeStorage
     * @param username
     * @param updated произошло ли обновление коллекции после выполнения команды. Грязный хак для передачи по ссылке
     * @return
     * @throws IOException
     * @throws NotFountId
     * @throws NoUniqueId
     * @throws ParserConfigurationException
     * @throws InvalidFieldException
     * @throws SQLException
     */
    public abstract String execute(ReadWriteInterface readWriteInterface,
                                   RouteStorage routeStorage,
                                   String username,
                                   Boolean[] updated) throws IOException, NotFountId, NoUniqueId, ParserConfigurationException, InvalidFieldException, SQLException;

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
