package ru.trickyfoxy.lab6.collection;

import org.xml.sax.SAXException;
import ru.trickyfoxy.lab6.exceptions.NoUniqueId;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    private String pathForSave;

    private final Date creationDate;

    private TreeSet<Route> storage = new TreeSetWithUniqueStricktlyAdd<Route>();

    private class TreeSetWithUniqueStricktlyAdd<E> extends TreeSet<E> {
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

    @XmlElement(name = "route")
    public TreeSet<Route> getStorage() {
        return storage;
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
    public String info() {
        StringBuilder sb = new StringBuilder();
        sb.append("Время инциализации коллекции: ").append(creationDate.toString()).append("\n");
        sb.append("Количество элементов в коллекции: ").append(storage.size()).append("\n");
        sb.append("Тип коллекции: ").append(storage.getClass().getName());
        return sb.toString();
    }

    @Override
    public String show() {
        StringBuilder sb = new StringBuilder();
        for (Route cur : storage) {
            sb.append(cur.toString()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String showSorted() {
        StringBuilder sb = new StringBuilder();
        ArrayList<Route> tmp = new ArrayList<>(storage);
        tmp.sort(Comparator.comparing(Route::getName));
        for (Route cur : tmp) {
            sb.append(cur.toString()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public boolean removeById(Long id) {
        return storage.remove(new Route(id));
    }

    @Override
    public void removeGreater(Route key) {
        while (storage.size() > 0 && storage.last().getDistance() > key.getDistance()) {
            removeById(storage.last().getId());
        }
    }

    @Override
    public boolean remove_any_by_distance(float distance) {
        for (Route cur : storage) {
            if (cur.getDistance() == distance) {
                removeById(cur.getId());
                return true;
            }
        }
        return false;
    }

    @Override
    public Route min_by_to() {
        Route mn = storage.last();
        for (Route cur : storage) {
            if (cur.getTo().compareTo(mn.getTo()) > 0) {
                mn = cur;
            }
        }
        return mn;
    }

    @Override
    public boolean update(long id, Route route) {
        return false;
    }

    @Override
    public boolean checkKey(long key) {
        return false;
    }

    @Override
    public int CountLessThanDistance(float distance) {
        int cnt = 0;
        for (Route cur : storage) {
            if (cur.getDistance() > distance) {
                cnt++;
            }
        }
        return cnt;
    }

    @Override
    public boolean add_if_max(Route route) {
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
        toXMLFile(pathForSave);
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

//        /**
//     * Заполнять Storage значениями из XML файла
//     *
//     * @param filePath Путь к XML файлу
//     * @throws IOException
//     * @throws SAXException
//     * @throws ParserConfigurationException
//     */
/*
    public void readRouteXMLFile(String filePath) throws IOException, SAXException, ParserConfigurationException, ParseException {


        if (!Files.exists(Paths.get(filePath))) {
            File f = new File(filePath);
            f.createNewFile();
            throw new FileNotFoundException("Файл не найден. Создан новый с таким же именем");
        }

        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(filePath);
        pathForSave = filePath;

        storage.clear();
        NodeList Routes = document.getDocumentElement().getChildNodes();
        for (int i = 0; i < Routes.getLength(); i++) {
            Node node = Routes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Route route = new Route();
                NodeList fields = node.getChildNodes();
                for (int indexCurrentRouteField = 0; indexCurrentRouteField < fields.getLength(); indexCurrentRouteField++) {
                    Node currentRouteField = fields.item(indexCurrentRouteField);
                    if (fields.item(indexCurrentRouteField).getNodeType() == Node.TEXT_NODE) {
                        continue;
                    }
                    switch (currentRouteField.getNodeName()) {
                        case "id": {
                            route.setId(Long.parseLong(currentRouteField.getTextContent()));
                            break;
                        }
                        case "name": {
                            route.setName(currentRouteField.getTextContent());
                            break;
                        }
                        case "coordinates": {
                            NodeList NodeCoordinates = currentRouteField.getChildNodes();
                            Coordinates coordinates = new Coordinates();

                            for (int k = 0; k < NodeCoordinates.getLength(); k++) {
                                if (NodeCoordinates.item(k).getNodeType() == Node.TEXT_NODE) {
                                    continue;
                                }
                                switch (NodeCoordinates.item(k).getNodeName()) {
                                    case "x":
                                        coordinates.setX(Float.parseFloat(NodeCoordinates.item(k).getTextContent()));
                                        break;
                                    case "y":
                                        coordinates.setY(Long.parseLong(NodeCoordinates.item(k).getTextContent()));
                                        break;
                                    default:
                                        throw new InvalidInputException("Неизвестное поле в классе LocationFrom");
                                }
                            }
                            route.setCoordinates(coordinates);
                            break;
                        }

                        case "creationDate": {
                            route.setCreationDate(new SimpleDateFormat("yyyy-MM-dd").parse(currentRouteField.getTextContent()));
                            break;
                        }
                        case "from": {
                            NodeList NodeLocationFrom = currentRouteField.getChildNodes();
                            LocationFrom from = new LocationFrom();
                            for (int k = 0; k < NodeLocationFrom.getLength(); k++) {
                                if (NodeLocationFrom.item(k).getNodeType() == Node.TEXT_NODE) {
                                    continue;
                                }
                                switch (NodeLocationFrom.item(k).getNodeName()) {
                                    case "x":
                                        from.setX(Integer.parseInt(NodeLocationFrom.item(k).getTextContent()));
                                        break;
                                    case "y":
                                        from.setY(Double.parseDouble(NodeLocationFrom.item(k).getTextContent()));
                                        break;
                                    case "z":
                                        from.setZ(Float.parseFloat(NodeLocationFrom.item(k).getTextContent()));
                                        break;
                                    case "name":
                                        from.setName(NodeLocationFrom.item(k).getTextContent());
                                        break;
                                    default:
                                        throw new InvalidInputException("Неизвестное поле в классе LocationFrom");
                                }
                            }
                            route.setFrom(from);
                            break;
                        }
                        case "to": {
                            NodeList NodeLocationTo = currentRouteField.getChildNodes();
                            LocationTo to = new LocationTo();
                            for (int k = 0; k < NodeLocationTo.getLength(); k++) {
                                if (NodeLocationTo.item(k).getNodeType() == Node.TEXT_NODE) {
                                    continue;
                                }
                                switch (NodeLocationTo.item(k).getNodeName()) {
                                    case "x":
                                        to.setX(Integer.parseInt(NodeLocationTo.item(k).getTextContent()));
                                        break;
                                    case "y":
                                        to.setY(Integer.parseInt(NodeLocationTo.item(k).getTextContent()));
                                        break;
                                    case "name":
                                        to.setName(NodeLocationTo.item(k).getTextContent());
                                        break;
                                    default:
                                        throw new InvalidInputException("Неизвестное поле в классе LocationFrom");
                                }
                            }
                            route.setTo(to);
                            break;
                        }
                        case "distance": {
                            route.setDistance(Float.parseFloat(currentRouteField.getTextContent()));
                            break;
                        }
                        default:
                            throw new InvalidInputException("Неизвестное поле в классе Route");

                    }
                }
                add(route);
            }
        }
    }
*/

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

    private class ByteArrayStreamOutputResolver extends SchemaOutputResolver {

        private ByteArrayOutputStream schemaOutputStream;

        public Result createOutput(String namespaceURI, String suggestedFileName) throws IOException {

            schemaOutputStream = new ByteArrayOutputStream(/*INITIAL_SCHEMA_BUFFER_SIZE*/10);
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

    public void toXMLFile(String filePath) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(RouteStorageImpl.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(this, new File(filePath));
    }

/*
    public void toXMLFile(String filePath) throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        Element root = doc.createElement("root");
        for (Route cur : storage) {
            Element route = doc.createElement("Route");
            {
                Element eId = doc.createElement("id");
                eId.setTextContent(String.valueOf(cur.getId()));
                route.appendChild(eId);
            }
            {
                Element eName = doc.createElement("name");
                eName.setTextContent(cur.getName());
                route.appendChild(eName);
            }
            {
                Element eCoo = doc.createElement("coordinates");
                Coordinates coordinates = cur.getCoordinates();
                Element eCooX = doc.createElement("x");
                eCooX.setTextContent(String.valueOf(coordinates.getX()));
                Element eCooY = doc.createElement("y");
                eCooY.setTextContent(String.valueOf(coordinates.getY()));
                eCoo.appendChild(eCooX);
                eCoo.appendChild(eCooY);
                route.appendChild(eCoo);
            }
            {
                Element eDate = doc.createElement("creationDate");
                eDate.setTextContent(new SimpleDateFormat("yyyy-MM-dd").format(cur.getCreationDate()));
                route.appendChild(eDate);
            }
            {
                Element From = doc.createElement("from");
                LocationFrom locationFrom = cur.getFrom();
                Element FromX = doc.createElement("x");
                FromX.setTextContent(String.valueOf(locationFrom.getX()));
                Element FromY = doc.createElement("y");
                FromY.setTextContent(String.valueOf(locationFrom.getY()));
                Element FromZ = doc.createElement("z");
                FromZ.setTextContent(String.valueOf(locationFrom.getZ()));
                Element FromName = doc.createElement("name");
                FromName.setTextContent(locationFrom.getName());
                From.appendChild(FromX);
                From.appendChild(FromY);
                From.appendChild(FromZ);
                From.appendChild(FromName);
                route.appendChild(From);
            }
            {
                Element To = doc.createElement("to");
                LocationTo locationTo = cur.getTo();
                Element ToX = doc.createElement("x");
                ToX.setTextContent(String.valueOf(locationTo.getX()));
                Element ToY = doc.createElement("y");
                ToY.setTextContent(String.valueOf(locationTo.getY()));
                Element ToName = doc.createElement("name");
                ToName.setTextContent(String.valueOf(locationTo.getName()));
                To.appendChild(ToX);
                To.appendChild(ToY);
                To.appendChild(ToName);
                route.appendChild(To);
            }
            {
                Element eDistance = doc.createElement("distance");
                eDistance.setTextContent(String.valueOf(cur.getDistance()));
                route.appendChild(eDistance);
            }
            root.appendChild(route);
        }
        doc.appendChild(root);
        writeDocument(doc, filePath);
    }
*/
/*
    private void writeDocument(Document document, String path)
            throws TransformerFactoryConfigurationError {
        Transformer trf = null;
        DOMSource src = null;
        FileOutputStream fos = null;
        try {
            trf = TransformerFactory.newInstance()
                    .newTransformer();
            src = new DOMSource(document);
            File f = new File(path);
            if (!Files.exists(Paths.get(path))) {
                f.createNewFile();
            }
            fos = new FileOutputStream(f);

            StreamResult result = new StreamResult(fos);
            trf.transform(src, result);
        } catch (TransformerException e) {
            e.printStackTrace(System.out);
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }*/
}

