package ru.trickyfoxy.lab7;

import ch.qos.logback.classic.Level;
import org.slf4j.LoggerFactory;
import ru.trickyfoxy.lab7.collection.RouteStorageImpl;
import ru.trickyfoxy.lab7.exceptions.ExitFromScriptException;
import ru.trickyfoxy.lab7.exceptions.InvalidLoginException;
import ru.trickyfoxy.lab7.exceptions.InvalidTokenExpiredException;
import ru.trickyfoxy.lab7.utils.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
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
    private static final int PORT = 1337;
    private static final int TIMEOUT = 500000;

    public boolean isReady = false;
    private final Map<String, Connect> sessionStorage = new HashMap<>();
    private final List<SocketChannel> notificationsList = new ArrayList<>();
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
            databaseManager = new DatabaseManager(db_config.uri, db_config.user, db_config.password);
            storage.setDatabaseManager(databaseManager);
            storage.pullRoutesFromDB();
        } catch (SQLException throwables) {
            logger.error("Беды с бдшкой");
            logger.error(throwables.getMessage());
            return;
        }

        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocketChannel.bind(new InetSocketAddress(PORT));
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
        List<SocketChannel> notificationsList;

        EventReaderThread(SocketChannel socketChannel,
                          Connect connect,
                          int timeout,
                          ForkJoinPool executor, List<SocketChannel> notificationsList) {
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
                } catch (IOException e) {
                    continue;
                } catch (TimeoutException e) {
                    if (event != null) {
                        sessionStorage.remove(event.session);
                    }
                    String message = "Disconnect " + socketChannel;
                    logger.error(message);
                    notifyAllUsers(message, Objects.requireNonNull(event).session);
                    break;
                }
                if (event != null) {
                    commonPool.execute(new HandleEvent(event, new Connect(), socketChannel));
                }
            }
        }
    }

    private void notifyAllUsers(String message, String session) {
        class SendAnswer extends RecursiveAction {
            final String message;
            final SocketChannel channel;

            public SendAnswer(String message, SocketChannel channel) {
                this.message = message;
                this.channel = channel;
            }

            @Override
            protected void compute() {
                try {
                    AnswerSender.sendAnswer(channel, new ServerAnswer("Ошибка сервера. Смотреть логи",
                            session,
                            ServerAnswerStatus.NOTIFICATION));
                    sessionStorage.get(session).expireTime = System.currentTimeMillis() + Connect.timeOfSessionLife;
                } catch (IOException e) {
                    logger.warn("Не удалось отправить");
                }
            }
        }
        for (SocketChannel channel : notificationsList) {
            commonPool.execute(new SendAnswer(message, channel));
        }
    }

    private boolean checkSession(String session, Connect connect) throws InvalidLoginException, InvalidTokenExpiredException {
        if (sessionStorage.get(session) != null
                && sessionStorage.get(session).expireTime > System.currentTimeMillis()) {
            connect.expireTime = System.currentTimeMillis() + Connect.timeOfSessionLife;
            return true;
        } else if (sessionStorage.get(session) == null) {
            throw new InvalidLoginException("Invalid token");
        } else if (sessionStorage.get(session).expireTime > System.currentTimeMillis()) {
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
                        notificationsList.add(socketChannel);
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

        void commandEventHandler() throws IOException {
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
                event.cmd.execute(new ReadWriteInterface(outputOfCommand), storage, sessionStorage.get(event.session).username);
                AnswerSender.sendAnswer(socketChannel, new ServerAnswer(outputOfCommand.toString(), connect.session));
            } catch (ExitFromScriptException e) {
                notifyAllUsers(("Disconnect " + socketChannel), Objects.requireNonNull(event).session);
            } catch (Exception e) {
                logger.error(e.getMessage());
                AnswerSender.sendAnswer(socketChannel, new ServerAnswer("Ошибка сервера. Смотреть логи", event.session, ServerAnswerStatus.SERVER_ERROR));
            }
        }
    }
}
