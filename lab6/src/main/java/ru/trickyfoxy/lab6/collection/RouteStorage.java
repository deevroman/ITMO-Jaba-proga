package ru.trickyfoxy.lab6.collection;

import ru.trickyfoxy.lab6.exceptions.NoUniqueId;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Date;

/**
 * Интерфейс коллекции. Описывание команды, которые требуется по заданию
 */
public interface RouteStorage {

    public Class<?> getCollectionClass();

    public Date getCreationDate();

    public String info();

    public String show();

    public String showSorted();

    public void add(Route route) throws NoUniqueId;

    public int size();

    void clear();

    boolean removeById(Long id);

    void removeGreater(Route key);

    boolean remove_any_by_distance(float distance);

    Route min_by_to();

    boolean update(long id, Route route);

    boolean checkKey(long key);

    int CountLessThanDistance(float distance);

    boolean add_if_max(Route route);

    boolean add_if_min(Route route);

    void save() throws IOException, ParserConfigurationException, JAXBException;

    void addAndGenId(Route route);

    boolean containsId(Long id);

    Route min();

    Route max();
}
