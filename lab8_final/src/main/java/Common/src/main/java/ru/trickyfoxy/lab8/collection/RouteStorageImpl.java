package ru.trickyfoxy.lab8.collection;

import ru.trickyfoxy.lab8.exceptions.NoUniqueId;
import ru.trickyfoxy.lab8.exceptions.NotFountId;
import ru.trickyfoxy.lab8.utils.DatabaseManager;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;

/**
 * Имплементация интерфейса RouteStorage
 */
@XmlRootElement(name = "storage")
public class RouteStorageImpl implements RouteStorage {

    @Override
    public Class<?> getCollectionClass() {
        return storage.getClass();
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    DatabaseManager databaseManager;

    private final Date creationDate;

    private TreeSet<Route> storage = new TreeSetWithUniqueStricktlyAdd<Route>();

    public void pullRoutesFromDB() throws SQLException {
        storage = databaseManager.getCollection().getStorage();
    }

    private class TreeSetWithUniqueStricktlyAdd<E> extends TreeSet<E> implements Serializable {
        public TreeSetWithUniqueStricktlyAdd() {

        }

        @Override
        public boolean add(E e) {
            if (contains(e))
                throw new NoUniqueId("Id в коллекции уже существует");
            return super.add(e);
        }

        public TreeSetWithUniqueStricktlyAdd(SortedSet<E> s) {
            super(s);
        }
    }

    @Override
    @XmlElement(name = "route")
    public TreeSet<Route> getStorage() {
        return storage;
    }

    public ArrayList<Route> getListElements() {
        return new ArrayList<>(storage);
    }

    public void setStorage(TreeSet<Route> storage) {
        for (Route route : storage) {
            add(route);
        }
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    public RouteStorageImpl() {
        creationDate = new Date();
    }

    public Boolean equalsStorage(RouteStorageImpl other) {
        if (storage.size() != other.storage.size()) {
            return false;
        }
        Route[] st = storage.toArray(new Route[0]);
        Route[] oth = other.storage.toArray(new Route[0]);

        for (int i = 0; i < storage.size(); i++) {
            if (!st[i].equals(oth[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    synchronized public String info() {
        return "Время инциализации коллекции: " + creationDate + "\n" +
                "Количество элементов в коллекции: " + storage.size() + "\n" +
                "Тип коллекции: " + storage.getClass().getName();
    }

    @Override
    synchronized public String show() {
        StringBuilder sb = new StringBuilder();
        for (Route cur : storage) {
            sb.append(cur.toString()).append("\n");
        }
        return sb.toString();
    }

    @Override
    synchronized public String showSorted() {
        StringBuilder sb = new StringBuilder();
        ArrayList<Route> tmp = new ArrayList<>(storage);
        tmp.sort(Comparator.comparing(Route::getName));
        for (Route cur : tmp) {
            sb.append(cur.toString()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    synchronized public void clear(String username) {
        List<Route> forDeleting = new ArrayList<>();
        for (Route cur : storage) {
            if (cur.getCreator().equals(username)) {
                forDeleting.add(cur);
            }
        }
        for (Route cur : forDeleting) {
            removeById(cur.getId());
        }
    }

    @Override
    synchronized public boolean removeById(Long id) {
        return storage.remove(new Route(id));
    }

    @Override
    synchronized public void removeGreater(Route key, String username) {
        List<Route> forDeleting = new ArrayList<>();
        for (Route cur : storage) {
            if (cur.getCreator().equals(username) && cur.getDistance() > key.getDistance()) {
                forDeleting.add(cur);
            }
        }
        for (Route cur : forDeleting) {
            removeById(cur.getId());
        }
    }

    @Override
    synchronized public boolean remove_any_by_distance(float distance) {
        for (Route cur : storage) {
            if (cur.getDistance() == distance) {
                removeById(cur.getId());
                return true;
            }
        }
        return false;
    }

    @Override
    synchronized public Route min_by_to() {
        Route mn = storage.last();
        for (Route cur : storage) {
            if (cur.getTo().compareTo(mn.getTo()) > 0) {
                mn = cur;
            }
        }
        return mn;
    }

    @Override
    synchronized public void update(long id, Route route) throws NotFountId {
        if (!this.containsId(id)) {
            throw new NotFountId("Такого id нет в коллекции");
        }
        route.setId(id);
        route.setCreationDate(new Date());
        this.removeById(id);
        this.add(route);
    }


    @Override
    synchronized public int CountLessThanDistance(float distance) {
        int cnt = 0;
        for (Route cur : storage) {
            if (cur.getDistance() > distance) {
                cnt++;
            }
        }
        return cnt;
    }

    @Override
    synchronized public boolean add_if_max(Route route) {
        if (size() > 1) {
            float mx_distance = storage.last().getDistance();
            for (Route cur : storage) {
                if (cur.getDistance() > mx_distance) {
                    mx_distance = cur.getDistance();
                }
            }
            if (route.getDistance() > mx_distance) {
                addAndGenId(route);
                return true;
            }
        } else {
            addAndGenId(route);
            return true;
        }
        return false;
    }

    @Override
    public boolean add_if_min(Route route) {
        if (size() > 1) {
            float mn_distance = storage.last().getDistance();
            for (Route cur : storage) {
                if (cur.getDistance() < mn_distance) {
                    mn_distance = cur.getDistance();
                }
            }
            if (route.getDistance() < mn_distance) {
                addAndGenId(route);
                return true;
            }
        } else {
            addAndGenId(route);
            return true;
        }
        return false;
    }

    @Override
    public void save() throws IOException, ParserConfigurationException, JAXBException {
//        toXMLFile(pathForSave);
    }

    public boolean containsId(Long id) {
        return storage.contains(new Route(id));
    }

    @Override
    public Route min() {
        return storage.first();
    }

    @Override
    public Route max() {
        return storage.last();
    }

    public Long generateId() {
        if (storage.isEmpty())
            return 1L;
        return storage.last().getId() + 1;
    }

    public void addAndGenId(Route route) {
        route.setId(generateId());
        storage.add(route);
    }

    public void add(Route route) throws NoUniqueId {
        if (containsId(route.getId())) {
            throw new NoUniqueId("Элемент с данным id уже существует");
        }
        storage.add(route);
    }

/*
    @Deprecated
    private void generateAndSetSchema(JAXBContext jaxbContext, Unmarshaller unmarshaller) throws SAXException, IOException {

        // generate schema
        ByteArrayStreamOutputResolver schemaOutput = new ByteArrayStreamOutputResolver();
        jaxbContext.generateSchema(schemaOutput);

        // load schema
        ByteArrayInputStream schemaInputStream = new ByteArrayInputStream(schemaOutput.getSchemaContent());
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(new StreamSource(schemaInputStream));

        // set schema on unmarshaller
        unmarshaller.setSchema(schema);
    }

    @Deprecated
    private class ByteArrayStreamOutputResolver extends SchemaOutputResolver {

        private ByteArrayOutputStream schemaOutputStream;

        public Result createOutput(String namespaceURI, String suggestedFileName) throws IOException {

            schemaOutputStream = new ByteArrayOutputStream(*/
    /*INITIAL_SCHEMA_BUFFER_SIZE*//*
10);
            StreamResult result = new StreamResult(schemaOutputStream);

            // We generate single XSD, so generator will not use systemId property
            // Nevertheless, it validates if it's not null.
            result.setSystemId("");

            return result;
        }

        public byte[] getSchemaContent() {
            return schemaOutputStream.toByteArray();
        }
    }

    @Deprecated
    public void readStorageXMLFile(String filePath) throws IOException, JAXBException, SAXException {
        if (!Files.exists(Paths.get(filePath))) {
            File f = new File(filePath);
            f.createNewFile();
            pathForSave = filePath;
            throw new FileNotFoundException("Файл не найден. Создан новый с таким же именем");
        }
        pathForSave = filePath;
        JAXBContext jaxbContext = JAXBContext.newInstance(RouteStorageImpl.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        generateAndSetSchema(jaxbContext, unmarshaller);
        unmarshaller.setEventHandler(event -> false);
        RouteStorageImpl rs = (RouteStorageImpl) unmarshaller.unmarshal(new File(filePath));
        storage = rs.storage;
    }

    @Deprecated
    public void toXMLFile(String filePath) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(RouteStorageImpl.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(this, new File(filePath));
    }
*/

}

