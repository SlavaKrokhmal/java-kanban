import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;

    private static final String FILE_PATH = "E:\\tasks.txt";

    public FileBackedTaskManager() {
        this(new File(FILE_PATH));
    }

    public FileBackedTaskManager(File file) {
        this.file = file;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            if (file.length() > 0) {
                loadFromFile(this.file);
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка инициализации файла", e);
        }
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    private void save() {
        try {
            List<Task> tasks = getAllTasks();
            String data = tasks.stream()
                    .map(this::taskToString)
                    .collect(Collectors.joining("\n"));
            Files.writeString(file.toPath(), data);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении задач", e);
        }
    }

    private String taskToString(Task task) {
        return task.getType() + "," + task.getId() + "," + task.getName() + "," + task.getDescription() + "," + task.getStatus();
    }

    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        String data = Files.readString(file.toPath());
        String[] tasks = data.split("\n");
        for (String taskData : tasks) {
            Task task = manager.taskFromString(taskData);
            manager.createTask(task);
        }
        return manager;
    }

    private Task taskFromString(String data) {
        String[] parts = data.split(",");
        TaskType type = TaskType.valueOf(parts[0]);
        int id = Integer.parseInt(parts[1]);
        String name = parts[2];
        String description = parts[3];
        Status status = Status.valueOf(parts[4]);
        Task task = new Task(name, description, status, type);
        task.setId(id);
        return task;
    }
}
