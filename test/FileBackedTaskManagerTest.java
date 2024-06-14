import manager.FileBackedTaskManager;
import model.Status;
import model.Task;
import model.TaskType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    private FileBackedTaskManager taskManager;
    private Path testFilePath;

    @BeforeEach
    void setUp() throws IOException {
        testFilePath = Paths.get("E:\\testTasks.txt");
        Files.deleteIfExists(testFilePath);
        Files.createFile(testFilePath);
        taskManager = new FileBackedTaskManager(testFilePath.toFile());
    }

    @Test
    void updatingTaskReflectsChanges() {
        Task task = new Task("Старое название", "Старое описание", Status.NEW, TaskType.TASK);
        taskManager.createTask(task);
        task.setName("Новое название");
        task.setDescription("Новое описание");
        taskManager.updateTask(task);

        Task updatedTask = taskManager.getTaskById(1);
        assertEquals("Новое название", updatedTask.getName());
        assertEquals("Новое описание", updatedTask.getDescription());
    }

    @Test
    void deletingTaskShouldResultInNull() {
        Task task = new Task("Задача", "Описание", Status.NEW, TaskType.TASK);
        taskManager.createTask(task);
        taskManager.deleteTask(1);

        assertNull(taskManager.getTaskById(1));
    }
}
