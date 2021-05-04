package ru.trickyfoxy.lab6.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class reciverAnswer {
    public static Object reciverAnswer (BufferedInputStream fromServer) throws IOException {
        byte[] bytes = new byte[1000000];
        int numberOfBytesRead = 1;
        byte[] resultBytes = new byte[0];
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        while (true) {
            numberOfBytesRead = fromServer.read(byteBuffer.array());
            if (numberOfBytesRead < 0) throw new SocketException();
            byte[] tempBytes = new byte[resultBytes.length + numberOfBytesRead];
            System.arraycopy(resultBytes, 0, tempBytes, 0, resultBytes.length);
            System.arraycopy(bytes, 0, tempBytes, resultBytes.length, numberOfBytesRead);
            resultBytes = tempBytes;
            byteBuffer.clear();
            try {
                return Serialization.DeserializeObject(resultBytes);
            } catch (ClassCastException e) {
                continue;
            }

        }
    }
}
