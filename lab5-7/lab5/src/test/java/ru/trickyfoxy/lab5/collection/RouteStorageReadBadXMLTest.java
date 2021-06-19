package ru.trickyfoxy.lab5.collection;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import ru.trickyfoxy.lab5.exceptions.InvalidFieldException;
import ru.trickyfoxy.lab5.exceptions.InvalidRouteFieldException;
import ru.trickyfoxy.lab5.exceptions.NoUniqueId;

import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.fail;

public class RouteStorageReadBadXMLTest {

    @Test
    void testReadXMLNotUniqueBadCase1() throws ParserConfigurationException, IOException, ParseException, SAXException, JAXBException {
        try {
            testReadXMLBad("readXML/testReadXMLNotUniqueBad1.xml");
        } catch (NoUniqueId e) {
            return;
        }
        fail();
    }

    @Test
    void testReadXMLPassedFieldBadCase1() throws ParserConfigurationException, IOException, ParseException, SAXException, JAXBException {
        try {
            testReadXMLBad("readXML/testReadXMLPassedFieldBad1.xml");
        } catch (Exception e) {
            return;
        }
        fail();
    }

    @Test
    void testReadXMLBrokenBadCase1() throws ParserConfigurationException, IOException, ParseException, SAXException, JAXBException {
        try {
            testReadXMLBad("readXML/testReadXMLBrokenBad1.xml");
        } catch (UnmarshalException e) {
            return;
        }
        fail();
    }

    @Test
    void testReadXMLUndefinedFieldBadCase1() throws ParserConfigurationException, IOException, ParseException, SAXException, JAXBException {
        try {
            testReadXMLBad("readXML/testReadXMLUndefinedFieldBad1.xml");
        } catch (UnmarshalException e) {
            return;
        }
        fail();
    }

    @Test
    void testReadXMLUndefinedFieldBadCase2() throws ParserConfigurationException, IOException, ParseException, SAXException, JAXBException {
        try {
            testReadXMLBad("readXML/testReadXMLUndefinedFieldBad2.xml");
        } catch (UnmarshalException e) {
            return;
        }
        fail();
    }

    @Test
    void testReadXMLFieldTypeBadCase1() throws ParserConfigurationException, IOException, ParseException, SAXException, JAXBException {
        try {
            testReadXMLBad("readXML/testReadXMLFieldTypeBad1.xml");
        } catch (UnmarshalException e) {
            return;
        }
        fail();
    }

    @Test
    void testReadXMLIncorrectFieldValueCase1() throws ParserConfigurationException, IOException, ParseException, SAXException, JAXBException {
        try {
            testReadXMLBad("readXML/testReadXMLIncorrectFieldValue1.xml");
        } catch (UnmarshalException | InvalidFieldException e) { // todo улучшить тип
            return;
        }
        fail();
    }

    @Test
    void testReadXMLIncorrectFieldValueCase2() throws ParserConfigurationException, IOException, ParseException, SAXException, JAXBException {
        try {
            testReadXMLBad("readXML/testReadXMLIncorrectFieldValue2.xml");
        } catch (UnmarshalException | InvalidFieldException e) { // todo улучшить тип
            return;
        }
        fail();
    }

    @Test
    void testReadXMLIncorrectFieldValueCase3() throws ParserConfigurationException, IOException, ParseException, SAXException, JAXBException {
        try {
            testReadXMLBad("readXML/testReadXMLIncorrectFieldValue3.xml");
        } catch (UnmarshalException | InvalidRouteFieldException e) { // todo улучшить тип
            return;
        }
        fail();
    }

    private void testReadXMLBad(String fileName) throws ParserConfigurationException, SAXException, IOException, NoUniqueId, ParseException, JAXBException {
        RouteStorageImpl testing = new RouteStorageImpl();
        testing.readStorageXMLFile("src/test/resources/" + fileName);
    }

}
