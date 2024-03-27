import java.util.*;

public class TaskManager {
    private int taskIdCounter = 1;
    private Map<Integer, Task> tasks;
    private Map<Integer, Epic> epics;
    private Map<Integer, Subtask> subtasks;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
    }

    // Метод для создания новой задачи
    public void createTask(Task task) {
        task.setId(taskIdCounter++);
        if (task instanceof Epic) {
            epics.put(task.getId(), (Epic) task);
        } else if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            int epicId = subtask.getEpicId();
            Epic epic = epics.get(epicId);
            if (epic != null) {
                subtask.setId(taskIdCounter++);
                subtasks.put(subtask.getId(), subtask);
                epic.addSubtask(subtask);
            } else {
                System.out.println("Эпик id " + epicId + " не найден. Подзадача не добавлена.");
                return;
            }
        }
        tasks.put(task.getId(), task);
    }

    // Метод для обновления задачи
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            if (task instanceof Epic) {
                epics.put(task.getId(), (Epic) task);
            } else if (task instanceof Subtask) {
                subtasks.put(task.getId(), (Subtask) task);
            }
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Задача с id " + task.getId() + " не найжена.");
        }
    }

    // Метод для обновления всех данных задачи
    public void updateTaskById(int id, Task newTask) {
        Task oldTask = tasks.get(id);
        if (oldTask != null) {
            newTask.setId(oldTask.getId());
            newTask.setStatus(newTask.getStatus());
            tasks.put(id, newTask);
            if (newTask instanceof Epic) {
                epics.put(id, (Epic) newTask);
            } else if (newTask instanceof Subtask) {
                subtasks.put(id, (Subtask) newTask);
            }
            System.out.println("Задача успешно обновлена.");
        } else {
            System.out.println("Задача с указанным id не найдена.");
        }
    }

    // Метод для удаления задачи по id
    public void deleteTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            tasks.remove(id);
            if (task instanceof Epic) {
                epics.remove(id);
            } else if (task instanceof Subtask) {
                Subtask subtask = (Subtask) task;
                int epicId = subtask.getEpicId();
                Epic epic = epics.get(epicId);
                if (epic != null) {
                    epic.getSubtasks().remove(task);
                }
                subtasks.remove(id);
            }
        } else {
            System.out.println("Задача с id " + id + " не найдена.");
        }
    }

    // Метод для получения задачи по id
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    // Метод для получения списка всех обычных задач
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    // Метод для получения списка всех эпиков
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    // Метод для получения списка всех подзадач
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }
}





