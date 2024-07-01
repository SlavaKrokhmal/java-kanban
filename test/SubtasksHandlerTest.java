import static org.junit.jupiter.api.Assertions.*;
import com.sun.net.httpserver.HttpServer;
import handlers.HttpTaskServer;
import handlers.SubtasksHandler;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;

public class SubtasksHandlerTest {
    private HttpTaskServer server;
    private HttpServer httpServer;
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() throws IOException {
        taskManager = new InMemoryTaskManager();
        server = new HttpTaskServer();
        httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/subtasks", new SubtasksHandler(taskManager));
        httpServer.start();
    }

    @AfterEach
    public void tearDown() {
        httpServer.stop(0);
    }

    @Test
    public void gettingAllSubtasksShouldReturnEmptyListInitially() throws IOException {
        URL url = new URL("http://localhost:8080/subtasks");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);

        connection.disconnect();
    }

    @Test
    public void deletingSubtaskByIdShouldRemoveSubtask() throws IOException {
        Epic epic = new Epic("тест эпика", "Описание", Status.NEW);
        taskManager.createTask(epic);
        Subtask subtask = new Subtask("тест подзадачи", "Описание", Status.NEW, epic.getId(), LocalDateTime.now(), Duration.ofMinutes(30));
        taskManager.createTask(subtask);
        int subtaskId = subtask.getId();

        URL url = new URL("http://localhost:8080/subtasks/" + subtaskId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);

        connection.disconnect();
    }
}
