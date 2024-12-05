package logic;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

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

        List<Task> updatedHistory = historyManager.getHistory();
        Task previeTask = updatedHistory.get(0);
        assertEquals(Status.NEW, previeTask.getStatus());

        Task actualTask = updatedHistory.get(1);
        assertEquals(Status.IN_PROGRESS, actualTask.getStatus());
    }

    // проверяем, что при удалении объекта, он удаляется и из истории
    @Test
    void removeTaskUsingRemoveHistoryTest() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Task taskBank = new Task("Банкомат", "Снять деньги", Status.NEW);
        Task taskStore = new Task("Магазин", "Купить продукты", Status.IN_PROGRESS);
        Task taskAlarmClock = new Task("Будильник", "Поставить будильник", Status.DONE);

        taskManager.addTask(taskBank);
        taskManager.addTask(taskStore);
        taskManager.addTask(taskAlarmClock);

        List<Task> historyBeforeDeleteTask = taskManager.getHistory();
        assertEquals(3, historyBeforeDeleteTask.size());

        taskManager.removeTask(taskStore.getTaskId());
        List<Task> historyAfterDeleteTask = taskManager.getHistory();
        assertEquals(2, historyAfterDeleteTask.size());
    }
}
