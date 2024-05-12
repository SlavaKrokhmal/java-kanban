import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    private HistoryManager historyManager;
    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
        task1 = new Task("Задача1", "Описание1", Status.NEW, TaskType.TASK);
        task2 = new Task("Задача2", "Описание2", Status.NEW, TaskType.TASK);

        task1.setId(1);
        task2.setId(2);
    }

    @Test
    void testHistoryPreservesPreviousVersion() {
        historyManager.add(task1);
        assertEquals(1, historyManager.getHistory().size(), "в истории должна быть 1 задача после добавления");

        task1.setDescription("обновили описание1");
        historyManager.add(task1);

        assertEquals(1, historyManager.getHistory().size(), "в истории должна остаться 1 задача после добавления.");
        Task retrievedTask = historyManager.getHistory().get(0);
        assertEquals("обновили описание1", retrievedTask.getDescription(), "Описание должно быть обновлено.");

        historyManager.add(task2);
        assertEquals(2, historyManager.getHistory().size(), "в истории должна быть 2 задача после добавления второй.");
        assertEquals(task2, historyManager.getHistory().get(1), "Вторая задача должна равняться добавленной.");
    }
}
