package ru.trickyfoxy.lab8.Client;

import ru.trickyfoxy.lab8.collection.Route;
import ru.trickyfoxy.lab8.commands.CommandsManager;
import ru.trickyfoxy.lab8.commands.GetElements;
import ru.trickyfoxy.lab8.exceptions.TimeoutConnectionException;
import ru.trickyfoxy.lab8.utils.*;
import ru.trickyfoxy.lab8.windows.MainWindow;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    public static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    private static Client instance;

    private static String HOST = "localhost";

    public static int getPORT() {
        return PORT;
    }

    private static int PORT = 1343;

    public static void setHOST(String HOST) {
        Client.HOST = HOST;
    }

    public static void setPORT(int PORT) {
        Client.PORT = PORT;
    }

    private static int TIMEOUT = 100000;
    public Connector connector = null;
    Scanner consoleInput = new Scanner(System.in);
    Thread notifier = null;


    public boolean authUser(String username, String password) throws TimeoutConnectionException, IOException {
        return auth(connector, username, password);
    }

    public void startPolling() {
        notifier = new Notifier(new Connector(new InetSocketAddress(HOST, PORT), TIMEOUT, connector.getSession()));
        notifier.start();
    }

    public ArrayList<Route> getAllElements() throws Exception {
        GetElements getElements = new GetElements();
        connector.reconnect();
        connector.sendCommand(getElements);
        ServerAnswer serverAnswer = (ServerAnswer) connector.receiver();
        if (serverAnswer.status == ServerAnswerStatus.OK) {
            return serverAnswer.storage;
        } else {
            throw new Exception(serverAnswer.message);
        }
    }

    public boolean regUser(String username, String password) throws TimeoutConnectionException, IOException {
        return register(connector, username, password);
    }

    private boolean auth(Connector connector, String username, String password) throws IOException, TimeoutConnectionException {
        connector.reconnect();
        connector.sendEvent(new Event(username, password, EventType.LOGIN));
        ServerAnswer result = ((ServerAnswer) connector.receiver());
        if (result.session_token != null) {
            connector.setSession(result.session_token);
            System.out.println("Вход произведён");
            startPolling();
            return true;
        } else {
            return false;
        }
    }

    public boolean register(Connector connector, String username, String password) throws IOException, TimeoutConnectionException {
        connector.reconnect();
        connector.sendEvent(new Event(username, password, EventType.REGISTER));
        ServerAnswer result = ((ServerAnswer) connector.receiver());
        if (result.session_token != null) {
            connector.setSession(result.session_token);
            return true;
        } else {
            return false;
        }
    }

    private static class LoginAndPass {
        String login;
        String pass;

        public LoginAndPass(String login, String pass) {
            this.login = login;
            this.pass = pass;
        }
    }

    public LoginAndPass askLoginAndPass() {
        System.out.println("Введите логин:");
        String username = consoleInput.nextLine();
        System.out.println("Введите пароль:");
        String password = consoleInput.nextLine();
        return new LoginAndPass(username, password);
    }

    public void init() throws IOException {

        connector = new Connector(new InetSocketAddress("localhost", PORT), TIMEOUT);
        connector.connect();

        CommandsManager.getInstance();
    }

    /**
     * Поток, который слушает уведомления от сервера
     */
    private static class Notifier extends Thread {
        Connector connector;

        public Notifier(Connector connector) {
            this.connector = connector;
        }

        @Override
        public void run() {
            while (true) {
                if (this.isInterrupted()) {
                    return;
                }
                try {
                    connector.connect();
                    connector.sendEvent(new Event(EventType.REGISTER_LISTEN_NOTIFIER));
                    ServerAnswer serverAnswer = (ServerAnswer) connector.receiver();
                    if (serverAnswer.storage != null) {
                        MainWindow.getInstance().refresh(serverAnswer.storage);
                        System.out.println("Прилетел апдейт с сервера");
                    } else {
                        System.out.println("Сообщение от сервера: " + serverAnswer.message);
                    }
                } catch (IOException | TimeoutConnectionException e) {
                    Connector notificationChannel = new Connector(connector);
                    try {
                        notificationChannel.connect();
                        notificationChannel.sendEvent(new Event(EventType.REGISTER_LISTEN_NOTIFIER));
                    } catch (IOException ioException) {
                        System.err.println("Не удаётся подключить канал уведомлений. Попробуйте зайти заново");
                        break;
                    }
                }
            }
        }
    }
}
