package manager;

import model.Task;
import model.Epic;
import model.Subtask;
import model.Status;
import history.HistoryManager;
import factory.Managers;
import exceptions.ManagerSaveException;

import java.util.*;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Comparator;
import java.time.LocalDateTime;

public class InMemoryTaskManager implements TaskManager {
    private int taskIdCounter = 1;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private static final Comparator<Task> startTimeComparator = Comparator.comparing(
            Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()));
    private final SortedSet<Task> prioritizedTasks = new TreeSet<>(startTimeComparator);

    @Override
    public void createTask(Task task) {
        boolean hasCross = prioritizedTasks.stream()
                .anyMatch(existingTask -> taskCross(task, existingTask));

        if (hasCross) {
            throw new ManagerSaveException("Невозможно добавить задачу, так как время выполнения пересекается с уже существующей задачей", null);
        }

        int id = taskIdCounter++;
        task.setId(id);
        if (task instanceof Epic) {
            epics.put(id, (Epic) task);
        } else if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.addSubtask(subtask);
                epic.updateDurationAndTime();
                if (epic.getStartTime() != null) {
                    prioritizedTasks.add(epic);
                }
            } else {
                System.out.println("Эпик с ID " + subtask.getEpicId() + " не найден. Подзадача не добавлена.");
            }
        } else {
            tasks.put(id, task);
            if (task.getStartTime() != null) {
                prioritizedTasks.add(task);
            }
        }
    }

    public boolean taskCross(Task t1, Task t2) {
        LocalDateTime start1 = t1.getStartTime();
        LocalDateTime end1 = t1.getEndTime();
        LocalDateTime start2 = t2.getStartTime();
        LocalDateTime end2 = t2.getEndTime();

        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    @Override
    public void updateTask(Task task) {
        if (task == null) {
            System.out.println("Задача для обновления не может быть null.");
            return;
        }

        if (task instanceof Epic) {
            Epic epic = (Epic) task;
            if (epics.containsKey(epic.getId())) {
                epics.put(epic.getId(), epic);
                epic.updateDurationAndTime();
                updateEpicStatus(epic);
                if (epic.getStartTime() != null) {
                    prioritizedTasks.add(epic);
                } else {
                    prioritizedTasks.remove(epic);
                }
            } else {
                System.out.println("Эпик с ID " + epic.getId() + " не найден.");
            }
        } else if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            if (subtasks.containsKey(subtask.getId())) {
                subtasks.put(subtask.getId(), subtask);
                Epic epic = epics.get(subtask.getEpicId());
                if (epic != null) {
                    epic.addSubtask(subtask);
                    epic.updateDurationAndTime();
                    updateEpicStatus(epic);
                    if (epic.getStartTime() != null) {
                        prioritizedTasks.add(epic);
                    } else {
                        prioritizedTasks.remove(epic);
                    }
                } else {
                    System.out.println("Эпик для подзадачи с ID " + subtask.getEpicId() + " не найден.");
                }
            } else {
                System.out.println("Подзадача с ID " + subtask.getId() + " не найдена.");
            }
        } else {
            if (tasks.containsKey(task.getId())) {
                tasks.put(task.getId(), task);
                if (task.getStartTime() != null) {
                    prioritizedTasks.add(task);
                } else {
                    prioritizedTasks.remove(task);
                }
            } else {
                System.out.println("Задача с ID " + task.getId() + " не найдена.");
            }
        }
    }

    @Override
    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else if (epics.containsKey(id)) {
            return epics.get(id);
        } else {
            return subtasks.get(id);
        }
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>(tasks.values());
        allTasks.addAll(epics.values());
        allTasks.addAll(subtasks.values());
        return allTasks;
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteTask(int id) {
        Task task = tasks.remove(id);
        if (task != null) {
            prioritizedTasks.remove(task);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getId());
                prioritizedTasks.remove(subtask);
                historyManager.remove(subtask.getId());
            }
            prioritizedTasks.remove(epic);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtask(subtask);
                epic.updateDurationAndTime();
                if (epic.getStartTime() == null) {
                    prioritizedTasks.remove(epic);
                } else {
                    prioritizedTasks.add(epic);
                }
            }
            prioritizedTasks.remove(subtask);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
        prioritizedTasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
        }
    }

    private void updateEpicStatus(Epic epic) {
        boolean allNew = true;
        boolean allDone = true;
        for (Subtask subtask : epic.getSubtasks()) {
            if (subtask.getStatus() != Status.NEW) {
                allNew = false;
            }
            if (subtask.getStatus() != Status.DONE) {
                allDone = false;
            }
        }
        if (allNew || epic.getSubtasks().isEmpty()) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            addTaskToHistory(task);
        }
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            addTaskToHistory(epic);
        }
        return epic;
    }

    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            addTaskToHistory(subtask);
        }
        return subtask;
    }

    private void addTaskToHistory(Task task) {
        historyManager.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}