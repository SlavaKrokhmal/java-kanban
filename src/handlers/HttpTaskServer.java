package handlers;

import manager.FileBackedTaskManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskType;
import model.Status;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeParseException;

public class HttpTaskServer {
    private static final Scanner scanner = new Scanner(System.in);
    private static final int PORT = 8080;
    private TaskManager taskManager = new InMemoryTaskManager();
    private HttpServer server;

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.startServer();
        httpTaskServer.printMenu();
    }

    public void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/tasks", new TasksHandler(taskManager));
        server.createContext("/subtasks", new SubtasksHandler(taskManager));
        server.createContext("/epics", new EpicsHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedTasksHandler(taskManager));

        server.start();
        System.out.println("Сервер 'слушает' порт " + PORT);
    }

    private void printMenu() {
        while (true) {
            System.out.println("\n===== Проверочное меню =====");
            System.out.println("1 - Создать задачу");
            System.out.println("2 - Вывести список задач");
            System.out.println("3 - Операции со статусами задач");
            System.out.println("4 - Обновление задачи");
            System.out.println("5 - Удалить задачу");
            System.out.println("6 - История просмотренных задач");
            System.out.println("7 - Загрузить из файла");
            System.out.println("0 - Выйти из программы");
            System.out.print("Выберите действие: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ввод должен быть числом. Попробуйте снова.");
                continue;
            }

            switch (choice) {
                case 1:
                    createTask();
                    break;
                case 2:
                    printAllTasks();
                    break;
                case 3:
                    manageTaskStatus();
                    break;
                case 4:
                    updateTaskById();
                    break;
                case 5:
                    deleteTaskMenu();
                    break;
                case 6:
                    printTaskHistory();
                    break;
                case 7:
                    loadTasksFromFile();
                    break;
                case 0:
                    stop();
                    System.out.println("Программа завершена.");
                    return;
                default:
                    System.out.println("Неправильный выбор. Попробуйте снова.");
            }
        }
    }

    private void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("Сервер остановлен.");
        }
    }

    private void printTaskHistory() {
        List<Task> history = taskManager.getHistory();
        if (history.isEmpty()) {
            System.out.println("История просмотров задач пуста.");
        } else {
            System.out.println("Последние просмотренные задачи:");
            for (Task task : history) {
                System.out.println(task);
            }
        }
    }

    private void loadTasksFromFile() {
        try {
            taskManager = new FileBackedTaskManager();
            System.out.println("Задачи успешно загружены из файла.");
        } catch (Exception e) {
            System.out.println("Произошла ошибка при загрузке задач: " + e.getMessage());
        }
    }

    private void deleteTaskMenu() {
        System.out.println("Какую задачу вы хотите удалить?");
        System.out.println("1 - Удалить задачу по id");
        System.out.println("2 - Удалить эпик по id");
        System.out.println("3 - Удалить подзадачу по id");
        System.out.println("4 - Удалить все задачи, эпики и подзадачи");
        System.out.print("Выберите опцию: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Введите ID для удаления: ");
                int taskId = scanner.nextInt();
                scanner.nextLine();
                taskManager.deleteTask(taskId);
                System.out.println("Задача удалена.");
                break;
            case 2:
                System.out.print("Введите ID для удаления: ");
                int epicId = scanner.nextInt();
                scanner.nextLine();
                taskManager.deleteEpic(epicId);
                System.out.println("Эпик удален.");
                break;
            case 3:
                System.out.print("Введите ID для удаления: ");
                int subtaskId = scanner.nextInt();
                scanner.nextLine();
                taskManager.deleteSubtask(subtaskId);
                System.out.println("Подзадача удалена.");
                break;
            case 4:
                taskManager.deleteAllTasks();
                System.out.println("Все задачи, эпики и подзадачи удалены.");
                break;
            default:
                System.out.println("Неверный выбор. Попробуйте снова.");
        }
    }

    private void createTask() {
        System.out.println("Выберите тип задачи:");
        System.out.println("1 - Обычная задача");
        System.out.println("2 - Эпик");
        System.out.println("3 - Подзадача");
        int typeChoice;
        try {
            typeChoice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ввод должен быть числом. Создание задачи отменено.");
            return;
        }

        System.out.print("Введите название задачи: ");
        String name = scanner.nextLine();
        System.out.print("Введите описание задачи: ");
        String description = scanner.nextLine();
        Status status;
        try {
            System.out.print("Введите статус задачи (NEW, IN_PROGRESS, DONE): ");
            status = Status.valueOf(scanner.nextLine().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Некорректный ввод статуса. Создание задачи отменено.");
            return;
        }

        System.out.print("Введите время начала задачи (yyyy-MM-ddTHH:mm): ");
        String dateTimeInput = scanner.nextLine();

        LocalDateTime startTime;
        try {
            if (dateTimeInput.isEmpty()) {
                System.out.println("Время начала задачи не введено. Создание задачи отменено.");
                return;
            }
            startTime = LocalDateTime.parse(dateTimeInput);
        } catch (DateTimeParseException e) {
            System.out.println("Некорректный ввод времени начала задачи. Пожалуйста, введите время в формате yyyy-MM-ddTHH:mm. Создание задачи отменено.");
            return;
        }
        System.out.print("Введите продолжительность задачи в минутах: ");
        long durationMinutes = scanner.nextLong();
        scanner.nextLine();

        Task task = null;
        switch (typeChoice) {
            case 1:
                task = new Task(name, description, status, TaskType.TASK, startTime, Duration.ofMinutes(durationMinutes));
                break;
            case 2:
                task = new Epic(name, description, status);
                break;
            case 3:
                System.out.print("Введите идентификатор эпика: ");
                int epicId = scanner.nextInt();
                scanner.nextLine();
                task = new Subtask(name, description, status, epicId, startTime, Duration.ofMinutes(durationMinutes));
                break;
            default:
                System.out.println("Неправильный выбор. Создание задачи отменено.");
        }

        if (task != null) {
            taskManager.createTask(task);
            System.out.println("Задача успешно создана.");
        }
    }

    private void printAllTasks() {
        System.out.println("Какую задачу вы хотите вывести?");
        System.out.println("1 - Вывести список всех задач, эпиков и подзадач по приоритету (время начала)");
        System.out.println("2 - Вывести задачу по ID");
        System.out.println("3 - Вывести эпик по ID");
        System.out.println("4 - Вывести подзадачу по ID");
        System.out.print("Выберите опцию: ");

        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1:
                List<Task> sortedTasks = ((InMemoryTaskManager) taskManager).getPrioritizedTasks();
                if (sortedTasks.isEmpty()) {
                    System.out.println("Список задач пуст.");
                } else {
                    sortedTasks.forEach(System.out::println);
                }
                break;
            case 2:
                System.out.print("Введите ID задачи для вывода: ");
                int taskId = scanner.nextInt();
                scanner.nextLine();
                Task task = taskManager.getTask(taskId);
                if (task != null) {
                    System.out.println(task);
                } else {
                    System.out.println("Задача с таким ID не найдена.");
                }
                break;
            case 3:
                System.out.print("Введите ID эпика для вывода: ");
                int epicId = scanner.nextInt();
                scanner.nextLine();
                Epic epic = taskManager.getEpic(epicId);
                if (epic != null) {
                    System.out.println(epic);
                } else {
                    System.out.println("Эпик с таким ID не найден.");
                }
                break;
            case 4:
                System.out.print("Введите ID подзадачи для вывода: ");
                int subtaskId = scanner.nextInt();
                scanner.nextLine();
                Subtask subtask = taskManager.getSubtask(subtaskId);
                if (subtask != null) {
                    System.out.println(subtask);
                } else {
                    System.out.println("Подзадача с таким ID не найдена.");
                }
                break;
            default:
                System.out.println("Неверный выбор. Попробуйте снова.");
                break;
        }
    }

    private void manageTaskStatus() {
        System.out.print("Введите идентификатор задачи для обновления статуса: ");
        int taskId = Integer.parseInt(scanner.nextLine());

        Task task = taskManager.getTaskById(taskId);
        if (task == null) {
            System.out.println("Задача с указанным идентификатором не найдена.");
            return;
        }

        Status newStatus;
        try {
            System.out.print("Введите новый статус для задачи (NEW, IN_PROGRESS, DONE): ");
            newStatus = Status.valueOf(scanner.nextLine().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Некорректный ввод статуса. Обновление задачи отменено.");
            return;
        }

        task.setStatus(newStatus);
        taskManager.updateTask(task);
        System.out.println("Статус задачи успешно обновлен.");
    }

    private void updateTaskById() {
        System.out.print("Введите идентификатор задачи для обновления: ");
        int taskId = Integer.parseInt(scanner.nextLine());

        Task oldTask = taskManager.getTaskById(taskId);
        if (oldTask == null) {
            System.out.println("Задача с указанным идентификатором не найдена.");
            return;
        }

        System.out.print("Введите название задачи: ");
        String name = scanner.nextLine();
        System.out.print("Введите описание задачи: ");
        String description = scanner.nextLine();
        Status status;
        try {
            System.out.print("Введите статус задачи (NEW, IN_PROGRESS, DONE): ");
            status = Status.valueOf(scanner.nextLine().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Некорректный ввод статуса. Обновление задачи отменено.");
            return;
        }

        Task newTask;
        Duration duration = oldTask.getDuration();
        LocalDateTime startTime = oldTask.getStartTime();
        if (oldTask instanceof Epic) {
            newTask = new Epic(name, description, status);
        } else if (oldTask instanceof Subtask) {
            newTask = new Subtask(name, description, status, ((Subtask) oldTask).getEpicId(), startTime, duration);
        } else {
            newTask = new Task(name, description, status, TaskType.TASK, startTime, duration);
        }
        newTask.setId(taskId);

        taskManager.updateTask(newTask);
        System.out.println("Задача успешно обновлена.");
    }

    private void deleteTask() {
        System.out.print("Введите идентификатор задачи для удаления: ");
        int taskId = Integer.parseInt(scanner.nextLine());

        taskManager.deleteTask(taskId);
        System.out.println("Задача успешно удалена.");
    }
}
