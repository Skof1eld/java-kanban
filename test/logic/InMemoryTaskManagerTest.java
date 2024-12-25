package logic;

import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private InMemoryHistoryManager historyManager;
    private Task hotel;
    private Task traveling;
    private Task purchases;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        hotel = new Task("Отель", "Забронировать номер", Status.NEW);
        traveling = new Task("Путешествие", "Купить билеты", Status.IN_PROGRESS);
        purchases = new Task("Покупки", "Купить продукты", Status.DONE);
    }

    // задачи добавляются в историю и сохраняют порядок
    @Test
    void shouldAddTaskToHistory() {
        hotel.setTaskId(1);
        traveling.setTaskId(2);
        purchases.setTaskId(3);

        historyManager.add(hotel);
        historyManager.add(traveling);
        historyManager.add(purchases);

        List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size());
        assertEquals(hotel, history.get(0));
        assertEquals(traveling, history.get(1));
        assertEquals(purchases, history.get(2));
    }

    // Удаление задачи из истории по идентификатору
    @Test
    void removeTaskFromHistory() {
        hotel.setTaskId(1);
        traveling.setTaskId(2);

        historyManager.add(hotel);
        historyManager.add(traveling);
        historyManager.remove(hotel.getTaskId());

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(traveling, history.getFirst());
    }

    // Возвращает пустой список, если в истории нет задач
    @Test
    void handleEmptyHistory() {
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty());
    }
}
