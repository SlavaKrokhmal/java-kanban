import factory.Managers;
import history.HistoryManager;
import model.Status;
import model.Task;
import model.TaskType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.Duration;

class HistoryManagerTest {

    private HistoryManager historyManager;
    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofHours(1);
        task1 = new Task("Задача1", "Описание1", Status.NEW, TaskType.TASK, startTime, duration);
        task2 = new Task("Задача2", "Описание2", Status.NEW, TaskType.TASK, startTime, duration);

        task1.setId(1);
        task2.setId(2);
    }

    @Test
    void addingToHistoryShouldPreservePreviousVersion() {
        historyManager.add(task1);
        Assertions.assertEquals(1, historyManager.getHistory().size(), "в истории должна быть 1 задача после добавления");

        task1.setDescription("обновили описание1");
        historyManager.add(task1);

        Assertions.assertEquals(1, historyManager.getHistory().size(), "в истории должна остаться 1 задача после добавления.");
        Task retrievedTask = historyManager.getHistory().get(0);
        Assertions.assertEquals("обновили описание1", retrievedTask.getDescription(), "Описание должно быть обновлено.");

        historyManager.add(task2);
        Assertions.assertEquals(2, historyManager.getHistory().size(), "в истории должна быть 2 задача после добавления второй.");
        Assertions.assertEquals(task2, historyManager.getHistory().get(1), "Вторая задача должна равняться добавленной.");
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task1.getId());
        Assertions.assertFalse(historyManager.getHistory().contains(task1), "Задача 1 должна быть удалена из истории.");
        Assertions.assertTrue(historyManager.getHistory().contains(task2), "Задача 2 должна оставаться в истории.");
    }

    @Test
    void accessingEmptyHistoryShouldReturnEmptyList() {
        Assertions.assertTrue(historyManager.getHistory().isEmpty(), "Доступ к пустой истории должен возвращать пустой список.");
    }


}
