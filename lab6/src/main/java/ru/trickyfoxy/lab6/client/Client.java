package ru.trickyfoxy.lab6.client;

import ru.trickyfoxy.lab6.collection.RouteStorageImpl;
import ru.trickyfoxy.lab6.commands.CommandsManager;
import ru.trickyfoxy.lab6.commands.Hello;
import ru.trickyfoxy.lab6.exceptions.ExitFromScriptException;
import ru.trickyfoxy.lab6.utils.ReadWriteInterface;
import ru.trickyfoxy.lab6.utils.reciverAnswer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    private static final int port = 1337;

    private static Scanner fromKeyboard;
    private static ObjectOutputStream toServer;
    private static ObjectInputStream fromServer;

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
            fromKeyboard = scanner;
            String host = "localhost";
            if (args.length > 0) {
                host = args[0];
            }
            while (true) {
                try (Socket outcoming = new Socket()) {
                    System.out.println("Подключение ...");
                    outcoming.connect(new InetSocketAddress(host, port), 2000);

                    BufferedInputStream fromServer = new BufferedInputStream(outcoming.getInputStream());
                    ObjectOutputStream toServer = new ObjectOutputStream(outcoming.getOutputStream());
                    toServer.writeObject(new Hello());
                    String result = (String) reciverAnswer.reciverAnswer(fromServer);
                    System.out.println(result);
                    outcoming.close();

                    try {
                        CommandsManager.getInstance().loop(readWriteInterface, storage, new InetSocketAddress(host, port));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (ExitFromScriptException e){
                        break;
                    }
                    break;
                } catch (IOException e) {
                    System.err.println("Не могу подключиться к " + host + ":" + port + ". Подключиться ещё раз? yes/no");
                    String answer;
                    while (!(answer = fromKeyboard.nextLine()).equals("yes")) {
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
