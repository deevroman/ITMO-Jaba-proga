package ru.trickyfoxy.lab5.collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.xml.sax.SAXException;
import ru.trickyfoxy.lab5.exceptions.NoUniqueId;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class RouteStorageTest {

    @TempDir
    public Path tempFolder;

    @Test
    void testSaveStorageToXML1() throws IOException, SAXException, ParserConfigurationException, NoUniqueId, ParseException, JAXBException {
        RouteStorageImpl routeStorage = new RouteStorageImpl();
        routeStorage.add(new Route(
                1L,
                "ЛОЛ",
                new Coordinates(48.0F, -48),
                new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01"),
                new LocationFrom(4, 42.9, (float) 1.2, "SPB"),
                new LocationTo(4, 41, "MSK"),
                19.2f
        ));
        routeStorage.add(new Route(
                13L,
                "КЕК",
                new Coordinates(4.0F, -8),
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"),
                new LocationFrom(41, 542.9, (float) 51.2, "QSPB"),
                new LocationTo(24, 341, "WMSK"),
                89.2f
        ));

        testSaveStorageToXML(routeStorage, "saveStorageToXML1/testSaveStorageToXML1.xml");
    }

    private void testSaveStorageToXML(RouteStorageImpl routeStorage, String testFileName) throws IOException, ParserConfigurationException, ParseException, SAXException, JAXBException {
        File tmpFile = File.createTempFile("jabalabtest-", ".tmp");
        routeStorage.toXMLFile(tmpFile.getPath());

        Path testFile = tmpFile.toPath();
        Path correctFile = Paths.get("src/test/resources/" + testFileName);

        // TODO по-хорошему нужно приводить XMl к одному виду и сравнивать содержимое файлов
        RouteStorageImpl testing = new RouteStorageImpl();
        testing.readStorageXMLFile(testFile.toString());

        RouteStorageImpl correct = new RouteStorageImpl();
        correct.readStorageXMLFile(correctFile.toString());

        assertTrue(testing.equalsStorage(correct));
    }

    @Test
    void testReadXMLCase1() throws IOException, SAXException, ParserConfigurationException, NoUniqueId, ParseException, JAXBException {
        RouteStorageImpl correct = new RouteStorageImpl();
        correct.add(new Route(
                1L,
                "ЛОЛ",
                new Coordinates(48.0F, -48),
                new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01"),
                new LocationFrom(4, 42.9, (float) 1.2, "SPB"),
                new LocationTo(4, 41, "MSK"),
                19.2f
        ));
        correct.add(new Route(
                13L,
                "КЕК",
                new Coordinates(4.0F, -8),
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"),
                new LocationFrom(41, 542.9, (float) 51.2, "QSPB"),
                new LocationTo(24, 341, "WMSK"),
                89.2f
        ));
        testReadXMLCase("readXML/testReadXML1.xml", correct);
    }

    @Test
    void testReadXMLFromNoCreatedFileCase1() throws IOException, SAXException, ParserConfigurationException, NoUniqueId, ParseException, JAXBException {
        RouteStorageImpl testing = new RouteStorageImpl();

        boolean ok = false;
        try {
            testing.readStorageXMLFile("src/test/resources/readXML/XMLFromNoCreatedFile.xml");
        } catch (FileNotFoundException e) {
            ok = true;
        }
        assertTrue(ok);
        assertTrue(testing.equalsStorage(new RouteStorageImpl()));

        assertTrue(Files.exists(Paths.get("src/test/resources/readXML/XMLFromNoCreatedFile.xml")));
        File toDelete = new File("src/test/resources/readXML/XMLFromNoCreatedFile.xml");
        toDelete.delete();
    }


    private void testReadXMLCase(String fileName, RouteStorageImpl correct) throws ParserConfigurationException, SAXException, IOException, NoUniqueId, ParseException, JAXBException {
        RouteStorageImpl testing = new RouteStorageImpl();
        testing.readStorageXMLFile("src/test/resources/" + fileName);
        assertTrue(testing.equalsStorage(correct));
    }

}