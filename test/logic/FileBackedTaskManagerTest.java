package logic;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    // Сохранение и загрузка пустого файла
    @Test
    void savingAndLoadEmptyFile() throws IOException {
        File tempFile = File.createTempFile("test", ".csv");
        tempFile.deleteOnExit();

        FileBackedTaskManager taskManager = new FileBackedTaskManager(tempFile);

        assertTrue(taskManager.getAllTasks().isEmpty());
        assertTrue(taskManager.getAllEpics().isEmpty());
        assertTrue(taskManager.getAllSubtasks().isEmpty());

        taskManager.save();
        FileBackedTaskManager uploadingData = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(uploadingData.getAllTasks().isEmpty());
        assertTrue(uploadingData.getAllEpics().isEmpty());
        assertTrue(uploadingData.getAllSubtasks().isEmpty());
    }

    // Сохранение и загрузка нескольких задач
    @Test
    void savingAndLoadMultipleTasks() throws IOException {
        File tempFile = File.createTempFile("test", ".csv");
        tempFile.deleteOnExit();

        FileBackedTaskManager taskManager = new FileBackedTaskManager(tempFile);

        Task purchases = new Task("Покупки", "Купить продукты", Status.NEW);
        taskManager.addTask(purchases);

        Epic traveling = new Epic("Путешествие", "Купить билеты");
        taskManager.addEpic(traveling);

        Subtask hotel = new Subtask("Отель", "Забронировать номер", Status.NEW, traveling.getTaskId());
        taskManager.addSubtask(hotel);

        taskManager.save();
        FileBackedTaskManager uploadingData = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(1, uploadingData.getAllTasks().size());
        assertEquals(1, uploadingData.getAllEpics().size());
        assertEquals(1, uploadingData.getAllSubtasks().size());

        Task loadedTask = uploadingData.getAllTasks().get(0);
        assertEquals(purchases.getNameOfTheTask(), loadedTask.getNameOfTheTask());
        assertEquals(purchases.getDescription(), loadedTask.getDescription());
        assertEquals(purchases.getStatus(), loadedTask.getStatus());

        Epic loadedEpic = uploadingData.getAllEpics().get(0);
        assertEquals(traveling.getNameOfTheTask(), loadedEpic.getNameOfTheTask());
        assertEquals(traveling.getDescription(), loadedEpic.getDescription());

        Subtask loadedSubtask = uploadingData.getAllSubtasks().get(0);
        assertEquals(hotel.getNameOfTheTask(), loadedSubtask.getNameOfTheTask());
        assertEquals(hotel.getDescription(), loadedSubtask.getDescription());
        assertEquals(hotel.getStatus(), loadedSubtask.getStatus());
        assertEquals(hotel.getEpicId(), loadedSubtask.getEpicId());
    }

    // "Сохранение и загрузка эпика без подзадач
    @Test
    void savingAndLoadEmptyEpic() throws IOException {
        File tempFile = File.createTempFile("test", ".csv");
        tempFile.deleteOnExit();

        FileBackedTaskManager taskManager = new FileBackedTaskManager(tempFile);

        Epic gasStation = new Epic("Заправка", "Заправить машину");
        taskManager.addEpic(gasStation);

        taskManager.save();
        FileBackedTaskManager uploadingData = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(1, uploadingData.getAllEpics().size());
        Epic loadedEpic = uploadingData.getAllEpics().get(0);
        assertEquals(gasStation.getNameOfTheTask(), loadedEpic.getNameOfTheTask());
        assertEquals(gasStation.getDescription(), loadedEpic.getDescription());
        assertTrue(uploadingData.getAllSubtasks().isEmpty());
    }
}
