package ru.trickyfoxy.lab8.utils;

import ru.trickyfoxy.lab8.commands.Command;
import ru.trickyfoxy.lab8.exceptions.TimeoutConnectionException;

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

    public String getSession() {
        return session;
    }

    String session;

    public Connector(Connector connector) {
        this.timeout = connector.timeout;
        this.session = connector.session;
        this.server = connector.server;
    }

    public void setSession(String session) {
        this.session = session;
    }

    private boolean connected = false;

    public Connector(InetSocketAddress server, int timeout) {
        this.server = server;
        this.timeout = timeout;
    }

    public Connector(InetSocketAddress server, int timeout, String session) {
        this.server = server;
        this.timeout = timeout;
        this.session = session;
    }

    public void reconnect() throws IOException {
        if(connected) {
            disconnect();
        }
        connect();
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

    public void sendEvent( Event event) throws IOException {
        System.out.println("Отправлено: " + event);
        event.session = session;
        toServer.writeObject(event);
    }

    public void sendCommand( Command cmd) throws IOException {
        toServer.writeObject(new Event(cmd, EventType.COMMAND, session));
        toServer.flush();
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
