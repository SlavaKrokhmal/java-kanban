import static org.junit.jupiter.api.Assertions.*;

import model.Status;
import model.Subtask;
import org.junit.jupiter.api.Test;
class SubtaskTest {
    @Test
    void equalSubtasksShouldBeEqual() {
        Subtask subtask1 = new Subtask("Подзадача1", "Описание1", Status.NEW, 1);
        Subtask subtask2 = new Subtask("Подзадача2", "Описание2", Status.IN_PROGRESS, 1);
        subtask1.setId(2);
        subtask2.setId(2);

        assertEquals(subtask1, subtask2, "Позадачи с одинаковым id должны быть равны");
    }
}