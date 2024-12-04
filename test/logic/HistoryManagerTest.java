package logic;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    // добавляемые задачи в HistoryManager, сохраняют предыдущую версию задачи.
    @Test
    public void taskAddedToHistoryManagerSavesPreviousVersion() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager taskManager = Managers.getDefault();

        Task buyGas = new Task("Заправить машину", "Съездить на заправку", Status.NEW);
        taskManager.addTask(buyGas);
        historyManager.add(buyGas);

        buyGas.setStatus(Status.IN_PROGRESS);
        historyManager.add(buyGas);

        ArrayList<Task> updatedHistory = historyManager.getHistory();
        Task previeTask = updatedHistory.get(0);
        assertEquals(Status.NEW, previeTask.getStatus());

        Task actualTask = updatedHistory.get(1);
        assertEquals(Status.IN_PROGRESS, actualTask.getStatus());
    }
}
