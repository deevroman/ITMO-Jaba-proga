package ru.trickyfoxy.lab6.utils;

import ru.trickyfoxy.lab6.commands.Command;
import ru.trickyfoxy.lab6.exceptions.TimeoutConnectionException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class Connector {
    InetSocketAddress server;
    int timeout;
    BufferedInputStream fromServer;
    ObjectOutputStream toServer;
    Socket outcoming;
    private boolean connected = false;

    public Connector(InetSocketAddress server, int timeout) {
        this.server = server;
        this.timeout = timeout;
    }

    public void connect() throws IOException {
        if(connected){
            return;
        }
        outcoming = new Socket();
        outcoming.connect(server, timeout);
        outcoming.setSoTimeout(timeout);

        fromServer = new BufferedInputStream(outcoming.getInputStream());
        toServer = new ObjectOutputStream(outcoming.getOutputStream());
        connected = true;
    }

    public void disconnect() throws IOException {
        outcoming.close();
        fromServer = null;
        toServer = null;
        outcoming = null;
        connected = false;
    }

    public void sendCommand( Command cmd) throws IOException {
        toServer.writeObject(cmd);
    }

    public Object receiver() throws IOException, TimeoutConnectionException {
        long m = System.currentTimeMillis();
        byte[] bytes = new byte[1000000];
        int numberOfBytesRead = 1;
        byte[] resultBytes = new byte[0];
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        while (true) {
            if ((double) (System.currentTimeMillis() - m) > timeout) {
                throw new TimeoutConnectionException();
            }
            numberOfBytesRead = fromServer.read(byteBuffer.array());
            if (numberOfBytesRead < 0) {
                throw new SocketException();
            }
            byte[] tempBytes = new byte[resultBytes.length + numberOfBytesRead];
            System.arraycopy(resultBytes, 0, tempBytes, 0, resultBytes.length);
            System.arraycopy(bytes, 0, tempBytes, resultBytes.length, numberOfBytesRead);
            resultBytes = tempBytes;
            byteBuffer.clear();
            try {
                return Serialization.DeserializeObject(resultBytes);
            } catch (ClassCastException | ClassNotFoundException e) {
                continue;
            }

        }
    }
}
