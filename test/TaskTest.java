import model.Status;
import model.Task;
import model.TaskType;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void equalTasksShouldBeEqual() {
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofHours(1);
        Task task1 = new Task("Задача1", "Описание1", Status.NEW, TaskType.TASK, startTime, duration);
        Task task2 = new Task("Задача2", "Описание2", Status.DONE, TaskType.TASK, startTime, duration);
        task1.setId(1);
        task2.setId(1);

        assertEquals(task1, task2, "Задачи с одинаковым id должны быть равны");
    }




    @Test
    void notEqualTasksShouldNotBeEqual() {
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofHours(1);
        Task task1 = new Task("Задача1", "Описание1", Status.NEW, TaskType.TASK, startTime, duration);
        Task task2 = new Task("Задача2", "Описание2", Status.DONE, TaskType.TASK, startTime, duration);
        task1.setId(1);
        task2.setId(2);

        assertNotEquals(task1, task2, "Задачи с одинаковым id не должны быть равны");
    }
}
