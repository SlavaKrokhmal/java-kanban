import java.util.*;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;
import model.Status;
import model.Task;
import model.Epic;
import model.Subtask;
import model.TaskType;
import exceptions.ManagerSaveException;
import java.io.IOException;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    protected abstract T createTaskManager();

    @BeforeEach
    public void setUp() throws IOException {
        taskManager = createTaskManager();
    }

    @Test
    public void createAndGetTaskShouldReturnNotNull() {
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofHours(1);
        Task task = new Task("Задача", "Описание", Status.NEW, TaskType.TASK, startTime, duration);
        taskManager.createTask(task);
        Assertions.assertNotNull(taskManager.getTask(task.getId()));
    }

    @Test
    public void updatingTaskShouldReflectChanges() {
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofHours(1);
        Task task = new Task("Старое название", "Описание", Status.NEW, TaskType.TASK, startTime, duration);
        taskManager.createTask(task);
        task.setName("Новое название");
        taskManager.updateTask(task);
        Assertions.assertEquals("Новое название", taskManager.getTask(task.getId()).getName());
    }

    @Test
    public void deletingTaskShouldResultInNull() {
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofHours(1);
        Task task = new Task("На удаление", "Описание", Status.NEW, TaskType.TASK, startTime, duration);
        taskManager.createTask(task);
        Assertions.assertNotNull(taskManager.getTask(task.getId()));
        taskManager.deleteTask(task.getId());
        Assertions.assertNull(taskManager.getTask(task.getId()));
    }

    @Test
    public void getAllTasksShouldReturnCorrectTaskCount() {
        LocalDateTime startTime1 = LocalDateTime.now();
        Duration duration1 = Duration.ofHours(1);
        taskManager.createTask(new Task("Задача1", "Описание", Status.NEW, TaskType.TASK, startTime1, duration1));

        LocalDateTime startTime2 = startTime1.plusHours(2);
        Duration duration2 = Duration.ofHours(1);
        taskManager.createTask(new Task("Задача2", "Описание", Status.NEW, TaskType.TASK, startTime2, duration2));

        Assertions.assertEquals(2, taskManager.getAllTasks().size());
    }

    @Test
    public void addSubtaskToEpicShouldUpdateEpicDurationAndTime() {
        Epic epic = new Epic("Название эпика", "Описание", Status.NEW);
        taskManager.createTask(epic);

        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofHours(2);
        Subtask subtask = new Subtask("Название подзадачи", "Описание подзадачи", Status.NEW, epic.getId(), startTime, duration);
        taskManager.createTask(subtask);

        if (subtask.getDuration() == null) {
            throw new IllegalStateException("Длительность подзадачи не установлена");
        }

        Epic updatedEpic = (Epic) taskManager.getEpic(epic.getId());
        Assertions.assertNotNull(updatedEpic.getStartTime(), "Время начала эпика должно быть установлено");
        Assertions.assertEquals(startTime, updatedEpic.getStartTime(), "Время начала эпика должно соответствовать времени начала подзадачи");
        Assertions.assertEquals(duration, updatedEpic.getDuration(), "Длительность эпика должна соответствовать длительности подзадачи");
    }

    @Test
    public void creatingOverlappingTasksShouldThrowException() {
        LocalDateTime startTime = LocalDateTime.now();
        Task task1 = new Task("Задача 1", "Описание", Status.NEW, TaskType.TASK, startTime, Duration.ofHours(3));
        taskManager.createTask(task1);

        Task task2 = new Task("Задача 2", "Описание", Status.NEW, TaskType.TASK, startTime.plusHours(1), Duration.ofHours(2));
        Assertions.assertThrows(ManagerSaveException.class, () -> taskManager.createTask(task2), "Должно быть исключение из-за пересечения времени выполнения");
    }

    @Test
    public void getPrioritizedTasksShouldReturnTasksInStartTimeOrder() {
        Task task1 = new Task("Задача 1", "Описание", Status.NEW, TaskType.TASK, LocalDateTime.now().plusHours(2), Duration.ofHours(1));
        taskManager.createTask(task1);

        Task task2 = new Task("Задача 2", "Описание", Status.NEW, TaskType.TASK, LocalDateTime.now(), Duration.ofHours(1));
        taskManager.createTask(task2);

        List<Task> tasks = taskManager.getPrioritizedTasks();
        Assertions.assertEquals(task2.getId(), tasks.get(0).getId());
        Assertions.assertEquals(task1.getId(), tasks.get(1).getId());
    }

    @Test
    public void addingTaskToHistoryAndRetrievingItShouldBeConsistent() {
        Task task = new Task("Задача", "Описание", Status.NEW, TaskType.TASK, LocalDateTime.now(), Duration.ofHours(1));
        taskManager.createTask(task);
        taskManager.getTask(task.getId());

        List<Task> history = taskManager.getHistory();
        Assertions.assertTrue(history.contains(task));
    }
}