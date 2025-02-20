package logic;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private File file;

    @Override
    public FileBackedTaskManager createTaskManager() {
        try {
            file = File.createTempFile("tasks", ".csv");
            return new FileBackedTaskManager(file);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать временный файл.", e);
        }
    }

    // сохранения и загрузки данных из файла
    @Test
    public void saveAndLoadFromFile() {
        Task task = new Task("Task 1", "Description", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        TaskManager.addTask(task);

        assertDoesNotThrow(() -> TaskManager.save());

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        Task loadedTask = loadedManager.getTask(task.getTaskId());
        assertNotNull(loadedTask);
        assertEquals(task, loadedTask);
    }

    // проверка на перехват исключения если файл не существует
    @Test
    public void testLoadFromFileWithInvalidFile() {
        File noFile = new File("the_file_does_not_exist.csv");   // файл не существует

        assertThrows(FileBackedTaskManager.ManagerSaveException.class, () -> FileBackedTaskManager.loadFromFile(noFile));
    }
}
