import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static TaskManager taskManager = new TaskManager();

    public static void main(String[] args) {
        printMenu();
    }

    private static void printMenu() {
        while (true) {
            System.out.println("\n===== Проверочное меню =====");
            System.out.println("1 - Создать задачу");
            System.out.println("2 - Вывести список всех задач, подзадач и эпиков");
            System.out.println("3 - Операции со статусами задач");
            System.out.println("4 - Обновление задачи");
            System.out.println("5 - Удалить задачу по id");
            System.out.println("6 - Удалить все эпики");
            System.out.println("7 - Удалить все задачи");
            System.out.println("8 - Удалить все подзадачи");
            System.out.println("0 - Выйти из программы");
            System.out.print("Выберите действие: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

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
                    deleteTask();
                    break;
                case 6:
                    taskManager.deleteAllEpics();
                    System.out.println("Все эпики успешно удалены.");
                    break;
                case 7:
                    taskManager.deleteAllTasks();
                    System.out.println("Все задачи успешно удалены.");
                    break;
                case 8:
                    taskManager.deleteAllSubtasks();
                    System.out.println("Все подзадачи успешно удалены.");
                    break;
                case 0:
                    System.out.println("Программа завершена.");
                    return;
                default:
                    System.out.println("Неправильный выбор. Попробуйте снова.");
            }
        }
    }

    private static void createTask() {
        System.out.println("Выберите тип задачи:");
        System.out.println("1 - Обычная задача");
        System.out.println("2 - Эпик");
        System.out.println("3 - Подзадача");
        int typeChoice = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Введите название задачи: ");
        String name = scanner.nextLine();
        System.out.print("Введите описание задачи: ");
        String description = scanner.nextLine();
        System.out.print("Введите статус задачи (NEW, IN_PROGRESS, DONE): ");
        Status status = Status.valueOf(scanner.nextLine().toUpperCase());

        Task task;
        switch (typeChoice) {
            case 1:
                task = new Task(name, description, status.ordinal());
                break;
            case 2:
                task = new Epic(name, description, status.ordinal());
                break;
            case 3:
                System.out.print("Введите идентификатор эпика: ");
                int epicId = scanner.nextInt();
                task = new Subtask(name, description, status.ordinal(), epicId);
                break;
            default:
                System.out.println("Неправильный выбор. Создание задачи отменено.");
                return;
        }

        taskManager.createTask(task);
        System.out.println("Задача успешно создана.");
    }

    private static void printAllTasks() {
        // Вывод обычных задач
        List<Task> allTasks = taskManager.getAllTasks();
        System.out.println("\n===== Список всех обычных задач =====");
        for (Task task : allTasks) {
            if (!(task instanceof Epic) && !(task instanceof Subtask)) {
                System.out.println(task);
            }
        }

        // Вывод эпиков
        List<Epic> allEpics = taskManager.getAllEpics();
        System.out.println("\n===== Список всех эпиков =====");
        for (Epic epic : allEpics) {
            System.out.println(epic);
        }

        // Вывод подзадач
        List<Subtask> allSubtasks = taskManager.getAllSubtasks();
        System.out.println("\n===== Список всех подзадач =====");
        for (Subtask subtask : allSubtasks) {
            System.out.println(subtask);
        }
    }

    private static void updateTaskById() {
        System.out.print("Введите идентификатор задачи для обновления: ");
        int taskId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Введите новые данные для задачи:");
        System.out.print("Введите название задачи: ");
        String name = scanner.nextLine();
        System.out.print("Введите описание задачи: ");
        String description = scanner.nextLine();
        System.out.print("Введите статус задачи (NEW, IN_PROGRESS, DONE): ");
        Status status = Status.valueOf(scanner.nextLine().toUpperCase());


        Task newTask = new Task(name, description, status.ordinal());

        taskManager.updateTaskById(taskId, newTask);
    }

    private static void manageTaskStatus() {
        System.out.print("Введите идентификатор задачи для обновления статуса: ");
        int taskId = scanner.nextInt();
        scanner.nextLine();

        Task task = taskManager.getTaskById(taskId);
        if (task == null) {
            System.out.println("Задача с указанным идентификатором не найдена.");
            return;
        }

        System.out.print("Введите новый статус для задачи (NEW, IN_PROGRESS, DONE): ");
        Status newStatus = Status.valueOf(scanner.nextLine().toUpperCase());
        task.setStatus(newStatus);
        taskManager.updateTask(task);
        System.out.println("Статус задачи успешно обновлен.");
    }

    private static void deleteTask() {
        System.out.print("Введите идентификатор задачи для удаления: ");
        int taskId = scanner.nextInt();
        scanner.nextLine();

        taskManager.deleteTask(taskId);
        System.out.println("Задача успешно удалена.");
    }
}
