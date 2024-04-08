import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testEqualTasks() {
        Task task1 = new Task("Задача1", "Описание1", Status.NEW);
        Task task2 = new Task("Задача2", "Описание2", Status.DONE);
        task1.setId(1);
        task2.setId(1);

        assertEquals(task1, task2, "Задачи с одинаковым id должны быть равны");
    }




    @Test
    void testNotEqualTasks() {
        Task task1 = new Task("Задача1", "Описание1", Status.NEW);
        Task task2 = new Task("Задача2", "Описание2", Status.DONE);
        task1.setId(1);
        task2.setId(2);

        assertNotEquals(task1, task2, "Задачи с одинаковым id не должны быть равны");
    }
}
