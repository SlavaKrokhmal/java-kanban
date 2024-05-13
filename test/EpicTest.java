import static org.junit.jupiter.api.Assertions.*;

import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.Test;
class EpicTest {


    @Test
    public void epicShouldNotAddItselfAsSubtask() {
        Epic epic = new Epic("Эпик", "Описание", Status.NEW);
        epic.setId(1);
        Subtask subtask = new Subtask("Подзадача", "Описание", Status.NEW, epic.getId());
        subtask.setId(2);

        try {
            epic.addSubtask(subtask);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Эпик не может быть добавлен в себя"));
        }
    }
    @Test
    void equalEpicsShouldBeEqual() {
        Epic epic1 = new Epic("Эпик1", "Описание1", Status.NEW);
        Epic epic2 = new Epic("Эпик2", "Описание2", Status.DONE);
        epic1.setId(3);
        epic2.setId(3);

        assertEquals(epic1, epic2, "Эпики с одинаковым id должны быть равны");
    }

}