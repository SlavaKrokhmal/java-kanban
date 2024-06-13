import manager.FileBackedTaskManager;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private Path testFilePath;

    @Override
    protected FileBackedTaskManager createTaskManager() {
        try {
            testFilePath = Paths.get("E:\\testTasks.txt");
            Files.deleteIfExists(testFilePath);
            Files.createFile(testFilePath);
            return new FileBackedTaskManager(testFilePath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Ошибка инициализации файла", e);
        }
    }
}
