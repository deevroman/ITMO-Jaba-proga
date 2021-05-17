//package ru.trickyfoxy.lab5;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.io.TempDir;
//import org.xml.sax.SAXException;
//import ru.trickyfoxy.lab5.collection.*;
//import ru.trickyfoxy.lab5.commands.CommandsManager;
//import ru.trickyfoxy.lab5.utils.ReadWriteInterface;
//
//import javax.xml.bind.JAXBException;
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Path;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//class MainTest {
//    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
//    private final PrintStream originalOut = System.out;
//    private final PrintStream originalErr = System.err;
//    private final String prefixTestDir = "src/test/resources/main/";
//
//    @BeforeEach
//    public void setUpStreams() {
//        System.setOut(new PrintStream(outContent));
//        System.setErr(new PrintStream(errContent));
//    }
//
//    @AfterEach
//    public void restoreStreams() {
//        System.setOut(originalOut);
//        System.setErr(originalErr);
//    }
//
//    @TempDir
//    public Path tempFolder;
//
////    @Test
////    public void testMainEmptyInput() throws FileNotFoundException {
////        System.setIn(new FileInputStream(prefixTestDir + "testEmptyInputMain/testEmptyInputMain.input"));
////        String[] args = new String[]{};
////        Main.main(args);
////        assertEquals("Укажите аргументом командной строки файл, для хранения коллекции", outContent.toString());
////    }
//
//    @Test
//    void testMainOnlyExitCommand() throws FileNotFoundException {
//        System.setIn(new FileInputStream(prefixTestDir + "testMainOnlyExitCommand/testMainOnlyExitCommand.input"));
//        String[] args = new String[]{"src/test/resources/readXML/testReadXML1.xml"};
//        Main.main(args);
//        assertEquals("", outContent.toString());
//    }
//
//    @Test
//    void testMainOnlyClearCommand() throws FileNotFoundException {
//        System.setIn(new FileInputStream(prefixTestDir + "testMainOnlyClearCommand/testMainOnlyClearCommand.input"));
//        String[] args = new String[]{"src/test/resources/readXML/testReadXML1.xml"};
//        Main.main(args);
//        assertEquals("Коллекция очищена\n", outContent.toString());
//    }
//
//    @Test
//    void testMainShowLoadedStorage() throws FileNotFoundException {
//        System.setIn(new FileInputStream(prefixTestDir + "testMainShowLoadedStorage/testMainShowLoadedStorage.input"));
//        String[] args = new String[]{prefixTestDir + "testMainShowLoadedStorage/storage.xml"};
//        Main.main(args);
//        assertEquals("Route{id=1, name='ЛОЛ', coordinates=Coordinates{x=48.0, y=-48}, creationDate=2020-01-01, from=LocationFrom{x=4, y=42.9, z=1.2, name='SPB'}, to=LocationTo{x=4, y=41, name='MSK'}, distance=19.2}\n" +
//                "Route{id=13, name='КЕК', coordinates=Coordinates{x=4.0, y=-8}, creationDate=2000-01-01, from=LocationFrom{x=41, y=542.9, z=51.2, name='QSPB'}, to=LocationTo{x=24, y=341, name='WMSK'}, distance=89.2}\n", outContent.toString());
//    }
//
//    @Test
//    void testMainAddAndShowStorage() throws FileNotFoundException {
//        System.setIn(new FileInputStream(prefixTestDir + "testMainAddAndShowStorage/testMainAddAndShowStorage.input"));
//        ReadWriteInterface readWriteInterface = new ReadWriteInterface(
//                new InputStreamReader(System.in, StandardCharsets.UTF_8),
//                new OutputStreamWriter(System.out, StandardCharsets.UTF_8),
//                true,
//                "");
//        RouteStorageImpl storage = new RouteStorageImpl();
//        CommandsManager.getInstance().loop(readWriteInterface, storage);
//
//        RouteStorageImpl correct = new RouteStorageImpl();
//        correct.add(new Route(1L,
//                "name",
//                new Coordinates(1F, 1),
//                new Date(),
//                new LocationFrom(1, 1D, 1F, "name"),
//                new LocationTo(1, 1, "name"),
//                2));
//        assertTrue(storage.equalsStorage(correct));
//    }
//
//    @Test
//    void testMainUpdateStorage() throws IOException, ParseException, JAXBException, SAXException {
//        System.setIn(new FileInputStream(prefixTestDir + "testMainUpdateCollectionStorage/testMainUpdateCollectionStorage.input"));
//        ReadWriteInterface readWriteInterface = new ReadWriteInterface(
//                new InputStreamReader(System.in, StandardCharsets.UTF_8),
//                new OutputStreamWriter(System.out, StandardCharsets.UTF_8),
//                true,
//                "");
//        RouteStorageImpl storage = new RouteStorageImpl();
//        storage.readStorageXMLFile("src/test/resources/main/testMainUpdateCollectionStorage/storage.xml");
//        CommandsManager.getInstance().loop(readWriteInterface, storage);
//
//        RouteStorageImpl correct = new RouteStorageImpl();
//        correct.add(new Route(
//                1L,
//                "ЛОЛ",
//                new Coordinates(48.0F, -48),
//                new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01"),
//                new LocationFrom(4, 42.9, (float) 1.2, "SPB"),
//                new LocationTo(4, 41, "MSK"),
//                19.2f
//        ));
//        correct.add(new Route(13L,
//                "name",
//                new Coordinates(1F, 1),
//                new Date(),
//                new LocationFrom(1, 1D, 1F, "name"),
//                new LocationTo(1, 1, "name"),
//                2));
//        assertTrue(storage.equalsStorage(correct));
//    }
//
//    @Test
//    void testMainOnlyInvalidCommand() throws FileNotFoundException {
//        System.setIn(new FileInputStream(prefixTestDir + "testMainOnlyInvalidCommand/testMainOnlyInvalidCommand.input"));
//        String[] args = new String[]{"src/test/resources/readXML/testReadXML1.xml"};
//        Main.main(args);
//        assertEquals("Неизвестная команда\n", outContent.toString());
//    }
//
//    @Test
//    void testMainLoadFromEmptyXml() throws FileNotFoundException {
//        System.setIn(new FileInputStream(prefixTestDir + "testMainLoadFromEmptyXml/testMainLoadFromEmptyXml.input"));
//        String[] args = new String[]{prefixTestDir + "testMainLoadFromEmptyXml/testMainLoadFromEmptyXml.xml"};
//        Main.main(args);
//        assertEquals("", outContent.toString());
//    }
//
//    @Test
//    void testMainLoadFromBadXml() throws FileNotFoundException {
//        System.setIn(new FileInputStream(prefixTestDir + "testMainLoadFromBadXml/testMainLoadFromBadXml.input"));
//        String[] args = new String[]{prefixTestDir + "testMainLoadFromBadXml/testMainLoadFromBadXml.xml"};
//        Main.main(args);
//        assertEquals("", outContent.toString());
//    }
//
//    @Test
//    void testRecursionCallScriptException() throws FileNotFoundException {
//        System.setIn(new FileInputStream(prefixTestDir + "testRecursionCallScriptException/testRecursionCallScriptException.input"));
//        String[] args = new String[]{prefixTestDir + "testRecursionCallScriptException/testRecursionCallScriptException.xml"};
//        Main.main(args);
//        assertEquals("", outContent.toString());
//    }
//}