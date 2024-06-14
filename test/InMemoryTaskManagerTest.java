import manager.InMemoryTaskManager;
import model.Status;
import model.Task;
import model.TaskType;
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
    public void createAndGetTaskShouldReturnNotNull() {
        Task task = new Task("Задача", "Описание", Status.NEW, TaskType.TASK);
        taskManager.createTask(task);
        Assertions.assertNotNull(taskManager.getTask(task.getId()));
    }

    @Test
    public void updatingTaskShouldReflectChanges() {
        Task task = new Task("Старое название", "Описание", Status.NEW, TaskType.TASK);
        taskManager.createTask(task);
        task.setName("Новое название");
        taskManager.updateTask(task);
        Assertions.assertEquals("Новое название", taskManager.getTask(task.getId()).getName());
    }

    @Test
    public void deletingTaskShouldResultInNull() {
        Task task = new Task("На удаление", "Описание", Status.NEW, TaskType.TASK);
        taskManager.createTask(task);
        Assertions.assertNotNull(taskManager.getTask(task.getId()));
        taskManager.deleteTask(task.getId());
        Assertions.assertNull(taskManager.getTask(task.getId()));
    }

    @Test
    public void getAllTasksShouldReturnCorrectTaskCount() {
        taskManager.createTask(new Task("Задача1", "Описание", Status.NEW, TaskType.TASK));
        taskManager.createTask(new Task("Задача2", "Описание", Status.NEW, TaskType.TASK));
        Assertions.assertEquals(2, taskManager.getAllTasks().size());
    }
}