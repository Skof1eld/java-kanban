package logic;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    //убедитесь, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров
    @Test
    void alwaysReturnsInitializedAndReadyToWorkInstancesOfManagers() {
        TaskManager taskManager = Managers.getDefault();

        Task buyGas = new Task("Заправить машину", "Съездить на заправку", Status.NEW);
        taskManager.addTask(buyGas);

        Task expected = taskManager.getTask(buyGas.getTaskId());
        assertEquals(expected, buyGas);
    }

    @Test
    void alwaysReturnsInitializedAndReadyToWorkInstancesOfHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager);

        Task buyGas = new Task("Заправить машину", "Съездить на заправку", Status.NEW);
        historyManager.add(buyGas);

        List<Task> actualList = historyManager.getHistory();
        assertEquals(1, actualList.size());
    }
}
