package ru.trickyfoxy.lab6.server;

import ru.trickyfoxy.lab6.collection.RouteStorageImpl;
import ru.trickyfoxy.lab6.commands.Command;
import ru.trickyfoxy.lab6.exceptions.NotFountId;
import ru.trickyfoxy.lab6.utils.ReadWriteInterface;
import ru.trickyfoxy.lab6.utils.Serialization;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Objects;

public class Server {
    private static final int port = 1337;
    private Socket incoming;
    Selector selector;

    public static void main(String[] args) {
        Server server = new Server();
        server.run(args);
    }

    public void run(String[] args) {
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
/*        try {
            selector = Selector.open();
//            We have to set connection host, port and non-blocking mode
            ServerSocketChannel socket = ServerSocketChannel.open();
            ServerSocket serverSocket = socket.socket();
            serverSocket.bind(new InetSocketAddress("localhost", port));
            socket.configureBlocking(false);
            socket.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> i = selectedKeys.iterator();

                while (i.hasNext()) {
                    SelectionKey key = i.next();

                    if (key.isAcceptable()) {
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = serverSocketChannel.accept(); // can be non-blocking
                        if (socketChannel != null) {
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                        }
                    } else if (key.isReadable()) {
                        System.out.println("Reading...");
                        {
                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(10000);
                            socketChannel.read(buffer); // can be non-blocking
                            buffer.flip();
                            try {
                                Object o = new Serialization().DeserializeObject(buffer.array());
                                if (o != null) {
                                    Command command = (Command) o;
                                    System.out.println("Received message: " + command);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            socketChannel.register(selector, SelectionKey.OP_WRITE, buffer);
                        }
                        {
                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();
                            socketChannel.write(buffer); // can be non-blocking
                            socketChannel.close();
                        }

//                        SocketChannel client = (SocketChannel) key.channel();
//
//                         Create buffer to read data
//                        ByteBuffer buffer = ByteBuffer.allocate(100000);
//                        client.read(buffer);
//                        try {
//                            Object o = new Serialization().DeserializeObject(buffer.array());
//                            if (o != null) {
//                                Command command = (Command) o;
//                                client.register(selector, SelectionKey.OP_WRITE);
//                                System.out.println("Received message: " + command);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                    }
                    i.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(port));
            System.out.print("Сервер начал слушать клиентов. " + "\nПорт " + port +
                    " / Адрес " + InetAddress.getLocalHost() + ".\nОжидаем подключения клиентов ");
            while (true) {
                SocketChannel socketChannel = serverSocketChannel.accept(); // non-blocking
                if (socketChannel == null) {
                    continue;
                }
                socketChannel.configureBlocking(false);
                Command cmd = getCommand(socketChannel);
                if (cmd != null) {
                    System.out.println("Received command: " + cmd);
                    Writer out = new StringWriter();
                    try {
                        cmd.execute(new ReadWriteInterface(out), storage);
                    } catch (Exception e) {
                        socketChannel.write(ByteBuffer.wrap(Objects.requireNonNull(Serialization.SerializeObject(e.getMessage()))));
                    }
                    socketChannel.write(ByteBuffer.wrap(Objects.requireNonNull(Serialization.SerializeObject(out.toString()))));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

/*
// Блокирующий
        try (ServerSocket server = new ServerSocket(port)) {

            System.out.print("Сервер начал слушать клиентов. " + "\nПорт " + server.getLocalPort() +
                    " / Адрес " + InetAddress.getLocalHost() + ".\nОжидаем подключения клиентов ");
            while (true) {
                incoming = server.accept();

                System.out.println(incoming + " подключился к серверу.");

                try (
                        ObjectInputStream getFromClient = new ObjectInputStream(incoming.getInputStream());
                        ObjectOutputStream sendToClient = new ObjectOutputStream(incoming.getOutputStream())) {
//                    sendToClient.writeObject("Соединение установлено.\nВы можете вводить команды.");
                    while (true) {
                        try {
                            Command requestFromClient = (Command) getFromClient.readObject();
                            System.out.print("Получено [" + requestFromClient + "] от " + incoming + ". ");
                            requestFromClient.execute(new ReadWriteInterface(sendToClient), storage);
                            System.out.println("Ответ успешно отправлен.");
                        } catch (SocketException e) {
                            System.out.println(incoming + " отключился от сервера.");
                            break;
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        } catch (NotFountId notFountId) {
                            notFountId.printStackTrace();
                        } catch (ExitFromScriptException e) {
                            System.out.println("Эта команда не выполняется на сервере. Как вы её отправили?)");
                        }
                    }
                } catch (IOException |
                        ClassNotFoundException ex) {
                    System.err.println(incoming + " отключился от сервера.");
                }
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }*/


    }

    public Command getCommand(SocketChannel socketChannel) throws IOException {
        byte[] bytes = new byte[100000];
        int numberOfBytesRead = 1;
        byte[] resultBytes = new byte[0];
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        Command response = null;
        while (true) {
            numberOfBytesRead = socketChannel.read(byteBuffer);
            if (numberOfBytesRead < 0) throw new SocketException();
            byte[] tempBytes = new byte[resultBytes.length + numberOfBytesRead];
            System.arraycopy(resultBytes, 0, tempBytes, 0, resultBytes.length);
            System.arraycopy(bytes, 0, tempBytes, resultBytes.length, numberOfBytesRead);
            resultBytes = tempBytes;
            byteBuffer.clear();
            try {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(resultBytes);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                response = (Command) objectInputStream.readObject();
                return response;
            } catch (IOException | ClassNotFoundException | ClassCastException e) {
                continue;
            }

        }
    }
}
