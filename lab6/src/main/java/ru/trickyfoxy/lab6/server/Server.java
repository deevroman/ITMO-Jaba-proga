package ru.trickyfoxy.lab6.server;

import ch.qos.logback.classic.Level;
import org.slf4j.LoggerFactory;
import ru.trickyfoxy.lab6.collection.RouteStorageImpl;
import ru.trickyfoxy.lab6.commands.Command;
import ru.trickyfoxy.lab6.server.utils.AnswerSender;
import ru.trickyfoxy.lab6.utils.ReadWriteInterface;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import static ru.trickyfoxy.lab6.server.utils.CommandReceiver.getCommand;

public class Server {
    private static final int port = 1337;
    private static final int timeout = 500000;
    private Selector selector;
    ch.qos.logback.classic.Logger logger;

    public static void main(String[] args) {
        Server server = new Server();
        server.run(args);
    }

    public class Connect {
        public byte[] byteArray = new byte[0];
        public double startTime = System.currentTimeMillis();

        public Connect() {

        }
    }

    public void run(String[] args) {
        logger =
                (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("com.baeldung.logback");

        logger.setLevel(Level.INFO);

        RouteStorageImpl storage = new RouteStorageImpl();
        if (args.length > 0) {
            try {
                storage.readStorageXMLFile(args[0]);
            } catch (IOException e) {
                logger.warn(e.getMessage());
            } catch (Exception e) {
                logger.warn(e.getMessage());
                return;
            }
        } else {
            System.err.println("Укажите аргументом командной строки файл, для хранения коллекции");
            return;
        }

        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocketChannel.bind(new InetSocketAddress(port));
            logger.info("Сервер начал слушать клиентов. ");
            logger.info("Порт " + port + " / Адрес " + InetAddress.getLocalHost());
            logger.info("Ожидаем подключения клиентов ");
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                int count = selector.select();
                if (count == 0) {
                    continue;
                }
                Set<SelectionKey> keySet = selector.selectedKeys();
                Iterator<SelectionKey> it = keySet.iterator();
                while (it.hasNext()) {
                    SelectionKey selectionKey = it.next();
                    it.remove();
                    if (selectionKey.isAcceptable()) {
                        SocketChannel channel = null;
                        try {
                            Socket socket = serverSocket.accept();
                            logger.info("Connection from: " + socket);
                            channel = socket.getChannel();
                        } catch (IOException e) {
                            logger.warn("Unable to accept channel");
                            e.printStackTrace();
                            selectionKey.cancel();
                        }
                        if (channel != null) {
                            try {
                                channel.configureBlocking(false);
                                channel.register(selector, SelectionKey.OP_READ);
                            } catch (IOException e) {
                                logger.warn("Unable to use channel");
                                e.printStackTrace();
                                selectionKey.cancel();
                            }
                        }
                    }
                    if (selectionKey.isReadable()) {
                        if (selectionKey.attachment() == null) {
                            selectionKey.attach(new Connect());
                        }
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        if (socketChannel == null) {
                            continue;
                        }
                        socketChannel.configureBlocking(false);
                        Command cmd = null;
                        try {
                            cmd = getCommand(socketChannel, (Connect) selectionKey.attachment(), timeout);
                        } catch (IOException e) {
                            continue;
                        } catch (TimeoutException e) {
                            logger.warn("Disconnect " + socketChannel);
                            selectionKey.cancel();
                        }
                        if (cmd != null) {
                            logger.info("Received command: " + cmd);
                            Writer outputOfCommand = new StringWriter();
                            try {
                                cmd.execute(new ReadWriteInterface(outputOfCommand), storage);
                            } catch (Exception e) {
                                AnswerSender.sendAnswer(socketChannel, e.getMessage());
                            }
                            AnswerSender.sendAnswer(socketChannel, outputOfCommand.toString());
                            selectionKey.cancel();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
