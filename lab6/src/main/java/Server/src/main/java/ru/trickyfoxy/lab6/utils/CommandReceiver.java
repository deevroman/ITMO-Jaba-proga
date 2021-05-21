package ru.trickyfoxy.lab6.utils;


import ru.trickyfoxy.lab6.Server;
import ru.trickyfoxy.lab6.commands.Command;

import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeoutException;

public class CommandReceiver {
    /**
     * @param socketChannel SocketChannel из которого нужно получить команду
     * @return Считанная команда
     * @throws TimeoutException Исключение при превышении времени ожидания
     */
    public static Command getCommand(SocketChannel socketChannel, Server.Connect connect, int timeout) throws IOException, TimeoutException {
        byte[] resultBytes = connect.byteArray;
        byte[] bytes = new byte[10000];
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        Command response = null;
        int numberOfBytesRead = 1;
        if ((System.currentTimeMillis() - connect.startTime) > timeout) {
            throw new TimeoutException();
        }
        numberOfBytesRead = socketChannel.read(byteBuffer);
        if (numberOfBytesRead < 0) {
            throw new SocketException();
        }
        byte[] tempBytes = new byte[resultBytes.length + numberOfBytesRead];
        System.arraycopy(resultBytes, 0, tempBytes, 0, resultBytes.length);
        System.arraycopy(bytes, 0, tempBytes, resultBytes.length, numberOfBytesRead);
        resultBytes = tempBytes;
        connect.byteArray = resultBytes;
        byteBuffer.clear();
        try {
            response = (Command) Serialization.DeserializeObject(resultBytes);
            return response;
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            return null;
        }
    }
}
