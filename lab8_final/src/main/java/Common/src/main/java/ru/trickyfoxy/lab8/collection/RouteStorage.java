package ru.trickyfoxy.lab8.collection;

import ru.trickyfoxy.lab8.exceptions.NoUniqueId;
import ru.trickyfoxy.lab8.exceptions.NotFountId;
import ru.trickyfoxy.lab8.utils.DatabaseManager;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.TreeSet;

/**
 * Интерфейс коллекции. Описывание команды, которые требуется по заданию
 */
public interface RouteStorage {

    Class<?> getCollectionClass();

    Date getCreationDate();

    String info();

    String show();

    String showSorted();

    void setDatabaseManager(DatabaseManager databaseManager);

    DatabaseManager getDatabaseManager();

    void add(Route route) throws NoUniqueId;

    int size();

    TreeSet<Route> getStorage();

    void clear(String username);

    boolean removeById(Long id);

    void removeGreater(Route key, String username);

    boolean remove_any_by_distance(float distance);

    Route min_by_to();

    void update(long id, Route route) throws NotFountId;


    int CountLessThanDistance(float distance);

    boolean add_if_max(Route route);

    boolean add_if_min(Route route);

    void save() throws IOException, ParserConfigurationException, JAXBException;

    void addAndGenId(Route route);

    boolean containsId(Long id);

    Route min();

    Route max();

    void pullRoutesFromDB() throws SQLException;
}
