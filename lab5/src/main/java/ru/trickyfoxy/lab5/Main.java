package ru.trickyfoxy.lab5;


import ru.trickyfoxy.lab5.collection.RouteStorageImpl;
import ru.trickyfoxy.lab5.commands.CommandsManager;
import ru.trickyfoxy.lab5.utils.ReadWriteInterface;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) {
        RouteStorageImpl storage = new RouteStorageImpl();
        if (args.length > 0) {
            try {
                storage.readStorageXMLFile(args[0]);
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
            } catch (IOException e) {
                System.err.println(e.getMessage());
            } catch (Exception e) {
                System.err.println(e.getMessage());
                return;
            }
        } else {
            System.out.println("Укажите аргументом командной строки файл, для хранения коллекции");
            return;
        }
        ReadWriteInterface readWriteInterface = new ReadWriteInterface(
                new InputStreamReader(System.in, StandardCharsets.UTF_8),
                new OutputStreamWriter(System.out, StandardCharsets.UTF_8),
                true,
                "");
        try {
            CommandsManager.getInstance().loop(readWriteInterface, storage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
