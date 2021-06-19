import org.junit.jupiter.api.Test;
import ru.trickyfoxy.lab7.Server;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {
    @Test
    void TestWork() {
        class ServerThread extends Thread {
            final Server server = new Server();

            public void run() {
                server.run();
            }
        }
        Thread serverThread = new ServerThread();
        serverThread.start();

    }
}