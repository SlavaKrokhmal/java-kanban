import static org.junit.jupiter.api.Assertions.*;
import com.sun.net.httpserver.HttpServer;
import handlers.HttpTaskServer;
import handlers.TasksHandler;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Status;
import model.Task;
import model.TaskType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;

public class TasksHandlerTest {
    private HttpTaskServer server;
    private HttpServer httpServer;
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() throws IOException {
        taskManager = new InMemoryTaskManager();
        server = new HttpTaskServer();
        httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/tasks", new TasksHandler(taskManager));
        httpServer.start();
    }

    @AfterEach
    public void tearDown() {
        httpServer.stop(0);
    }

    @Test
    public void gettingAllTasksShouldReturnEmptyListInitially() throws IOException {
        URL url = new URL("http://localhost:8080/tasks");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);

        connection.disconnect();
    }

    @Test
    public void gettingNonexistentTaskShouldReturn404() throws IOException {
        URL url = new URL("http://localhost:8080/tasks/999");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(404, responseCode);

        connection.disconnect();
    }

    @Test
    public void postingTaskShouldCreateNewTask() throws IOException {
        URL url = new URL("http://localhost:8080/tasks");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.getOutputStream().write("{\"название\":\"тестовая задача\",\"описание\":\"Описание\",\"Статус\":\"NEW\",\"время начала\":\"2023-06-25T10:15:30\",\"продолжительность\":60}".getBytes());

        int responseCode = connection.getResponseCode();
        assertEquals(201, responseCode);

        connection.disconnect();
    }

    @Test
    public void deletingTaskByIdShouldRemoveTask() throws IOException {
        taskManager.createTask(new Task("тестовая задача", "Описание", Status.NEW, TaskType.TASK, LocalDateTime.now(), Duration.ofMinutes(60)));
        int taskId = taskManager.getAllTasks().get(0).getId();

        URL url = new URL("http://localhost:8080/tasks/" + taskId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);

        connection.disconnect();
    }
}
