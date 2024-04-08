import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class InMemoryTaskManagerTest {
    private InMemoryTaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void testCreateAndGetTask() {
        Task task = new Task("Задача", "Описание", Status.NEW);
        taskManager.createTask(task);
        Assertions.assertNotNull(taskManager.getTask(task.getId()));
    }

    @Test
    public void testUpdateTask() {
        Task task = new Task("Старое название", "Описание", Status.NEW);
        taskManager.createTask(task);
        task.setName("Новое название");
        taskManager.updateTask(task);
        Assertions.assertEquals("Новое название", taskManager.getTask(task.getId()).getName());
    }

    @Test
    public void testDeleteTask() {
        Task task = new Task("На удаление", "Описание", Status.NEW);
        taskManager.createTask(task);
        Assertions.assertNotNull(taskManager.getTask(task.getId()));
        taskManager.deleteTask(task.getId());
        Assertions.assertNull(taskManager.getTask(task.getId()));
    }

    @Test
    public void testGetAllTasks() {
        taskManager.createTask(new Task("Задача1", "Описание", Status.NEW));
        taskManager.createTask(new Task("Задача2", "Описание", Status.NEW));
        Assertions.assertEquals(2, taskManager.getAllTasks().size());
    }
}