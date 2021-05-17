package ru.trickyfoxy.lab6.client;

import ru.trickyfoxy.lab6.client.utils.Connector;
import ru.trickyfoxy.lab6.collection.RouteStorageImpl;
import ru.trickyfoxy.lab6.commands.CommandsManager;
import ru.trickyfoxy.lab6.exceptions.ExitFromScriptException;
import ru.trickyfoxy.lab6.exceptions.TimeoutConnectionException;
import ru.trickyfoxy.lab6.utils.ReadWriteInterface;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;



public class Client {
    private static final int port = 1337;
    private static final int timeout = 500000;

    public static void main(String[] args) {
        Client client = new Client();
        client.run(args);
    }

    public void run(String[] args) {
        RouteStorageImpl storage = new RouteStorageImpl();
        ReadWriteInterface readWriteInterface = new ReadWriteInterface(
                new InputStreamReader(System.in, StandardCharsets.UTF_8),
                new OutputStreamWriter(System.out, StandardCharsets.UTF_8),
                true,
                "");
        try (Scanner scanner = new Scanner(System.in)) {
            String host = "localhost";
            if (args.length > 0) {
                host = args[0];
            }
            while (true) {
                try {
                    System.out.println("Подключение ...");
                    Connector connector = new Connector(new InetSocketAddress(host, port), timeout);
                    connector.connect();
                    System.out.println("Соединение установлено.");
                    connector.disconnect();

                    try {
                        CommandsManager.getInstance().loop(readWriteInterface, storage, connector);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (ExitFromScriptException e) {
                        break;
                    }
                    break;
                } catch (IOException | TimeoutConnectionException e) {
                    System.err.println("Не могу подключиться к " + host + ":" + port + ". Подключиться ещё раз? yes/no");
                    String answer;
                    while (!(answer = scanner.nextLine()).equals("yes")) {
                        switch (answer) {
                            case "":
                                break;
                            case "no":
                                return;
                            default:
                                System.out.println("yes/no?");
                        }
                    }

                }
            }
        }
    }
}
