import java.util.*;

public class TaskManager {
    private int taskIdCounter = 1;
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Epic> epics;
    private final Map<Integer, Subtask> subtasks;

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
                epic.setStatus(Status.IN_PROGRESS);
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
            } else if (newTask instanceof Subtask updatedSubtask) {
                int epicId = updatedSubtask.getEpicId();
                Epic epic = epics.get(epicId);
                if (epic != null) {
                    subtasks.put(id, updatedSubtask);
                    updateEpicStatus(epic);
                } else {
                    System.out.println("Эпик с id " + epicId + " не найден.");
                }
            }
            System.out.println("Задача успешно обновлена.");
        } else {
            System.out.println("Задача с указанным id не найдена.");
        }
    }

    // Метод для обновления статуса эпика в зависимости от статусов его подзадач
    private void updateEpicStatus(Epic epic) {
        List<Subtask> subtasks = epic.getSubtasks();
        boolean allNew = true;
        boolean allDone = true;

        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() != Status.NEW) {
                allNew = false;
            }
            if (subtask.getStatus() != Status.DONE) {
                allDone = false;
            }
        }

        if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    // Метод для удаления задачи по id
    public void deleteTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            tasks.remove(id);
            if (task instanceof Epic) {
                epics.remove(id);
                Epic epic = (Epic) task;
                List<Subtask> subtasksToRemove = epic.getSubtasks();
                for (Subtask subtask : subtasksToRemove) {
                    subtasks.remove(subtask.getId());
                }
                updateEpicStatus(epic);
            } else if (task instanceof Subtask) {
                Subtask subtask = (Subtask) task;
                int epicId = subtask.getEpicId();
                Epic epic = epics.get(epicId);
                if (epic != null) {
                    epic.getSubtasks().remove(subtask);
                    updateEpicStatus(epic);
                }
                subtasks.remove(id);
            }
        } else {
            System.out.println("Задача с id " + id + " не найдена.");
        }
    }

    // Метод для удаления всех эпиков
    public void deleteAllEpics() {
        List<Subtask> subtasksToDelete = new ArrayList<>();
        for (Epic epic : epics.values()) {
            List<Subtask> epicSubtasks = epic.getSubtasks();
            subtasksToDelete.addAll(epicSubtasks);
            epicSubtasks.clear();
        }
        epics.clear();
        for (Subtask subtask : subtasksToDelete) {
            subtasks.remove(subtask.getId());
        }
    }

    // Метод для удаления всех задач
    public void deleteAllTasks() {
        List<Task> tasksToDelete = new ArrayList<>();

        for (Task task : tasks.values()) {
            if (!(task instanceof Epic) && !(task instanceof Subtask)) {
                tasksToDelete.add(task);
            }
        }

        for (Task task : tasksToDelete) {
            tasks.remove(task.getId());
        }
    }

    // Метод для удаления всех подзадач
    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
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





