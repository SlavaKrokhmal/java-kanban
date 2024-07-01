import java.util.List;
import history.HistoryManager;
import history.InMemoryHistoryManager;
import model.Status;
import model.Task;
import model.TaskType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.Duration;

class InMemoryHistoryManagerTest {

    @Test
    public void addingTasksToHistoryShouldPreservePreviousVersion() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofHours(1);
        Task task1 = new Task("Задача1", "Описание1", Status.NEW, TaskType.TASK, startTime, duration);
        Task task2 = new Task("Задача2", "Описание2", Status.DONE, TaskType.TASK, startTime, duration);
        task1.setId(1);
        task2.setId(2);
        historyManager.add(task1);
        historyManager.add(task2);
        List<Task> history = historyManager.getHistory();
        Assertions.assertTrue(history.contains(task1) && history.contains(task2), "В истории должно быть 2 задачи.");
    }

    @Test
    public void removingTaskShouldRemoveItFromHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofHours(1);
        Task task1 = new Task("Задача1", "Описание1", Status.NEW, TaskType.TASK, startTime, duration);
        Task task2 = new Task("Задача2", "Описание2", Status.DONE, TaskType.TASK, startTime, duration);
        task1.setId(1);
        task2.setId(2);

        historyManager.add(task1);
        historyManager.add(task2);
        Assertions.assertEquals(2, historyManager.getHistory().size(), "История должна содержать 2 задачи перед удалением.");

        historyManager.remove(1);
        List<Task> history = historyManager.getHistory();
        Assertions.assertFalse(history.contains(task1), "Задача1 должна быть удалена из истории.");
        Assertions.assertTrue(history.contains(task2), "Задача2 должна остаться в истории.");
    }

}