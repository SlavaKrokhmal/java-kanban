
import java.time.LocalDateTime;
import java.time.Duration;

import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
class EpicTest {


    @Test
    public void epicShouldNotAddItselfAsSubtask() {
        Epic epic = new Epic("Эпик", "Описание", Status.NEW);
        epic.setId(1);
        LocalDateTime startTime = LocalDateTime.now();
        Subtask subtask = new Subtask("Подзадача", "Описание", Status.NEW, epic.getId(), startTime, Duration.ofHours(1));
        subtask.setId(2);

        try {
            epic.addSubtask(subtask);
        } catch (IllegalArgumentException e) {
            Assertions.assertTrue(e.getMessage().contains("Эпик не может быть добавлен в себя"));
        }
    }
    @Test
    void equalEpicsShouldBeEqual() {
        Epic epic1 = new Epic("Эпик1", "Описание1", Status.NEW);
        Epic epic2 = new Epic("Эпик2", "Описание2", Status.DONE);
        epic1.setId(3);
        epic2.setId(3);

        Assertions.assertEquals(epic1, epic2, "Эпики с одинаковым id должны быть равны");
    }

}