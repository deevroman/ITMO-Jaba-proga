package ru.trickyfoxy.lab8;

import ch.qos.logback.classic.Level;
import org.slf4j.LoggerFactory;
import ru.trickyfoxy.lab8.collection.RouteStorageImpl;
import ru.trickyfoxy.lab8.exceptions.ExitFromScriptException;
import ru.trickyfoxy.lab8.exceptions.InvalidLoginException;
import ru.trickyfoxy.lab8.exceptions.InvalidTokenExpiredException;
import ru.trickyfoxy.lab8.utils.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeoutException;

public class Server {
    private int PORT = 1343;
    private final int TIMEOUT = 100000;

    public class ChannelForNotification {
        SocketChannel socketChannel;
        String session;

        public ChannelForNotification(SocketChannel socketChannel, String session) {
            this.socketChannel = socketChannel;
            this.session = session;
        }
    }

    public boolean isReady = false;
    private final Map<String, Connect> sessionStorage = new HashMap<>();
    private final List<ChannelForNotification> notificationsList = new ArrayList<>();
    private final RouteStorageImpl storage = new RouteStorageImpl();
    DatabaseManager databaseManager = null;
    ForkJoinPool commonPool = ForkJoinPool.commonPool();

    ch.qos.logback.classic.Logger logger;

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }

    public void run() {
        logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("com.baeldung.logback");
        logger.setLevel(Level.INFO);

        try {
            databaseManager = new DatabaseManager(DatabaseConfig.URI, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
            storage.setDatabaseManager(databaseManager);
            storage.pullRoutesFromDB();
        } catch (SQLException throwables) {
            logger.error("Беды с бдшкой");
            logger.error(throwables.getMessage());
            return;
        }

        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            ServerSocket serverSocket = serverSocketChannel.socket();
            while (PORT <= 1400) {
                try {
                    serverSocketChannel.bind(new InetSocketAddress(PORT));
                    break;
                } catch (BindException e) {
                    PORT++;
                    if (PORT > 1400) {
                        logger.error("Не удаётся занять порты");
                        return;
                    }
                }
            }
            logger.info("Сервер начал слушать клиентов. ");
            logger.info("Порт " + PORT + " / Адрес " + serverSocketChannel.getLocalAddress());
            isReady = true;
            while (true) {
                Socket socket = serverSocket.accept();
                logger.info("Connection from: " + socket);
                SocketChannel socketChannel = socket.getChannel();
                Thread eventReaderThread = new EventReaderThread(socketChannel, new Connect(), TIMEOUT, commonPool, notificationsList);
                eventReaderThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class EventReaderThread extends Thread {
        final SocketChannel socketChannel;
        final Connect connect;
        final int timeout;
        final ForkJoinPool executor;
        List<ChannelForNotification> notificationsList;

        EventReaderThread(SocketChannel socketChannel,
                          Connect connect,
                          int timeout,
                          ForkJoinPool executor, List<ChannelForNotification> notificationsList) {
            this.socketChannel = socketChannel;
            this.connect = connect;
            this.timeout = timeout;
            this.executor = executor;
            this.notificationsList = notificationsList;
        }

        @Override
        public void run() {
            Event event = null;

            while (true) {
                try {
                    event = EventReceiver.getEvent(socketChannel, connect, timeout);
                    try {
                        // Сделано для долгих запросы, чтобы их постоянная проверка не создавала нагрузку
                        sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    continue;
                } catch (TimeoutException e) {
                    sessionStorage.remove(connect.session);
                    String message = "Disconnect " + socketChannel;
                    logger.error(message);
//                    notifyAllUsers(new ServerAnswer(message), connect.session);
                    break;
                }
                if (event != null) {
                    commonPool.execute(new HandleEvent(event, new Connect(), socketChannel));
                    if (event.onetimeQuery) {
                        break;
                    }
                    event = null;
                }
            }
        }
    }

    private void notifyAllUsers(ServerAnswer message, String session) {
        class SendAnswer extends RecursiveAction {
            final ServerAnswer serverAnswer;
            final ChannelForNotification channel;

            public SendAnswer(ServerAnswer message, ChannelForNotification channel) {
                this.serverAnswer = message;
                this.channel = channel;
            }

            @Override
            protected void compute() {
                try {
                    AnswerSender.sendAnswer(channel.socketChannel, new ServerAnswer(serverAnswer.message,
                            channel.session,
                            ServerAnswerStatus.NOTIFICATION,
                            storage.getListElements()));
                    sessionStorage.get(channel.session).sessionExpireTime = System.currentTimeMillis() + Connect.timeOfSessionLife;
                } catch (IOException e) {
                    logger.warn("Не удалось отправить");
                }
            }
        }
        logger.info("Рассылаю уведомление");
        for (ChannelForNotification channel : notificationsList) {
            commonPool.execute(new SendAnswer(message, channel));
        }
    }

    private boolean checkSession(String session, Connect connect) throws InvalidLoginException, InvalidTokenExpiredException {
        if (sessionStorage.get(session) != null
                && sessionStorage.get(session).sessionExpireTime > System.currentTimeMillis()) {
            connect.sessionExpireTime = System.currentTimeMillis() + Connect.timeOfSessionLife;
            return true;
        } else if (sessionStorage.get(session) == null) {
            throw new InvalidLoginException("Invalid token");
        } else if (sessionStorage.get(session).sessionExpireTime > System.currentTimeMillis()) {
            throw new InvalidTokenExpiredException("Token expired");
        }
        return false;
    }

    private class HandleEvent extends RecursiveAction {
        Event event;
        Connect connect;
        SocketChannel socketChannel;

        public HandleEvent(Event event, Connect connect, SocketChannel socketChannel) {
            this.event = event;
            this.connect = connect;
            this.socketChannel = socketChannel;
        }

        @Override
        protected void compute() {
            if (event == null) {
                return;
            }
            try {
                logger.info(event.toString());
                switch (event.eventType) {
                    case COMMAND: {
                        commandEventHandler();
                        break;
                    }
                    case REGISTER: {
                        registerEventHandler();
                        break;
                    }
                    case LOGIN: {
                        loginEventHandler();
                        break;
                    }
                    case REGISTER_LISTEN_NOTIFIER: {
                        registerListenNotifierEventHandler();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void registerListenNotifierEventHandler() throws IOException {
            try {
                if (checkSession(event.session, connect)) {
                    try {
                        notificationsList.add(new ChannelForNotification(socketChannel, event.session));
                        logger.info(socketChannel.socket() + " добавлен в список рассылки");
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        AnswerSender.sendAnswer(socketChannel, new ServerAnswer("Ошибка сервера. Смотреть логи", event.session, ServerAnswerStatus.SERVER_ERROR));
                    }
                }
            } catch (InvalidLoginException e) {
                logger.error("Попытка выполнить команду с неавторизованным токеном");
                AnswerSender.sendAnswer(socketChannel, new ServerAnswer("Invalid token", null, ServerAnswerStatus.AUTH_ERROR));
                socketChannel.finishConnect();
            } catch (InvalidTokenExpiredException e) {
                logger.error("Попытка выполнить команду с истёкшим токеном");
                AnswerSender.sendAnswer(socketChannel, new ServerAnswer("Token expired", null, ServerAnswerStatus.AUTH_ERROR));
                socketChannel.finishConnect();
            }
        }

        private void loginEventHandler() throws IOException {
            try {
                if (databaseManager.authClient(event.login, event.pass)) {
                    logger.info("Пользователь " + event.login + " авторизован");
                    connect.createSession(databaseManager.generateSession());
                    connect.username = event.login;
                    sessionStorage.put(connect.session, connect);
                    AnswerSender.sendAnswer(socketChannel, new ServerAnswer("ok", connect.session));
                } else {
                    logger.warn("Неудачная попытка зайти под пользователем " + event.login);
                    AnswerSender.sendAnswer(socketChannel, new ServerAnswer("Неправильный логин/пароль", null, ServerAnswerStatus.AUTH_ERROR));
                    socketChannel.finishConnect();
                }
            } catch (SQLException throwables) {
                logger.error("Не могу подключиться к бд");
            }
        }

        private void registerEventHandler() throws IOException {
            try {
                if (databaseManager.register(event.login, event.pass)) {
                    logger.info("Пользователь " + event.login + " зарегистрирован");
                    connect.createSession(databaseManager.generateSession());
                    AnswerSender.sendAnswer(socketChannel, new ServerAnswer("ok", connect.session));
                } else {
                    logger.warn("Опаньки");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (InvalidLoginException e) {
                AnswerSender.sendAnswer(socketChannel, new ServerAnswer(e.getMessage(), "", ServerAnswerStatus.CLIENT_ERROR));
            }
            socketChannel.finishConnect();
        }

        private void commandEventHandler() throws IOException {
            try {
                checkSession(event.session, connect);
            } catch (InvalidLoginException e) {
                logger.error("Попытка выполнить команду с неавторизованным токеном");
                AnswerSender.sendAnswer(socketChannel, new ServerAnswer("Invalid token", null, ServerAnswerStatus.AUTH_ERROR));
                socketChannel.finishConnect();
            } catch (InvalidTokenExpiredException e) {
                logger.error("Попытка выполнить команду с истёкшим токеном");
                AnswerSender.sendAnswer(socketChannel, new ServerAnswer("Token expired", null, ServerAnswerStatus.AUTH_ERROR));
                socketChannel.finishConnect();
            }
            try {
                Writer outputOfCommand = new StringWriter();
                if (event.cmd.getName().equals("getElements")) {
                    AnswerSender.sendAnswer(socketChannel, new ServerAnswer(storage.getListElements(), connect.session));
                } else {
                    Boolean[] updated = new Boolean[]{Boolean.FALSE};
                    event.cmd.execute(new ReadWriteInterface(outputOfCommand), storage, sessionStorage.get(event.session).username, updated);
                    AnswerSender.sendAnswer(socketChannel, new ServerAnswer(outputOfCommand.toString(), connect.session));
                    if (updated[0]) {
                        notifyAllUsers(new ServerAnswer(), event.session);
                    }
                }

            } catch (ExitFromScriptException e) {
                notifyAllUsers(new ServerAnswer("Disconnect " + socketChannel), Objects.requireNonNull(event).session);
            } catch (Exception e) {
                logger.error(e.getMessage());
                AnswerSender.sendAnswer(socketChannel, new ServerAnswer("Ошибка сервера. Смотреть логи", event.session, ServerAnswerStatus.SERVER_ERROR));
            }
        }
    }
}
