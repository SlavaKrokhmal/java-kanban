import static org.junit.jupiter.api.Assertions.*;
import com.sun.net.httpserver.HttpServer;
import handlers.EpicsHandler;
import handlers.HttpTaskServer;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

public class EpicsHandlerTest {
    private HttpTaskServer server;
    private HttpServer httpServer;
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() throws IOException {
        taskManager = new InMemoryTaskManager();
        server = new HttpTaskServer();
        httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/epics", new EpicsHandler(taskManager));
        httpServer.start();
    }

    @AfterEach
    public void tearDown() {
        httpServer.stop(0);
    }

    @Test
    public void gettingAllEpicsShouldReturnEmptyListInitially() throws IOException {
        URL url = new URL("http://localhost:8080/epics");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);

        connection.disconnect();
    }

    @Test
    public void gettingNonexistentEpicShouldReturn404() throws IOException {
        URL url = new URL("http://localhost:8080/epics/999");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(404, responseCode);

        connection.disconnect();
    }

    @Test
    public void deletingEpicByIdShouldRemoveEpic() throws IOException {
        Epic epic = new Epic("тест эпика", "Описание", Status.NEW);
        taskManager.createTask(epic);
        int epicId = epic.getId();

        URL url = new URL("http://localhost:8080/epics/" + epicId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);

        connection.disconnect();
    }
}
