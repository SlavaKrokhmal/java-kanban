import java.util.List;

public interface TaskManager {
    void createTask(Task task);

    void updateTask(Task task);

    Task getTaskById(int id);

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    void deleteTask(int id);

    void deleteAllEpics();

    void deleteAllTasks();

    void deleteAllSubtasks();

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    List<Task> getHistory();
}