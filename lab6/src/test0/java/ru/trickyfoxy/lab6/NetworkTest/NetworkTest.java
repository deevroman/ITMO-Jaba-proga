package ru.trickyfoxy.lab6.NetworkTest;

import org.junit.jupiter.api.Test;
import ru.trickyfoxy.lab6.server.Server;

class NetworkTest {
    @Test
    void testCreateServer() throws InterruptedException {
        class ServerThread extends Thread {
            public void run() {
                Server server = new Server();
                server.run(new String[]{"test.xml"});
            }
        }
        ServerThread serverThread = new ServerThread();
        serverThread.start();
        Thread.sleep(4000);

    }

}