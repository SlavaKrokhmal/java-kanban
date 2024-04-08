import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
class InMemoryHistoryManagerTest {

    @Test
    public void addingTasksToHistoryShouldPreservePreviousVersion() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Задача1", "Описание1", Status.NEW);
        Task task2 = new Task("Задача2", "Описание2", Status.DONE);
        task1.setId(1);
        task2.setId(2);
        historyManager.add(task1);
        historyManager.add(task2);
        List<Task> history = historyManager.getHistory();
        assertTrue(history.contains(task1) && history.contains(task2), "В истории должно быть 2 задачи.");
    }

}