import static org.junit.jupiter.api.Assertions.*;
import com.sun.net.httpserver.HttpServer;
import handlers.HttpTaskServer;
import handlers.PrioritizedTasksHandler;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

public class PrioritizedTasksHandlerTest {
    private HttpTaskServer server;
    private HttpServer httpServer;
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() throws IOException {
        taskManager = new InMemoryTaskManager();
        server = new HttpTaskServer();
        httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/prioritized", new PrioritizedTasksHandler(taskManager));
        httpServer.start();
    }

    @AfterEach
    public void tearDown() {
        httpServer.stop(0);
    }

    @Test
    public void gettingAllPrioritizedTasksShouldReturnEmptyListInitially() throws IOException {
        URL url = new URL("http://localhost:8080/prioritized");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);

        connection.disconnect();
    }
}
