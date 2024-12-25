package logic;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    HistoryManager historyManager = Managers.getDefaultHistory();

    // Порядок задач в истории сохраняется после добавления и удаления.
    @Test
    void orderTasksInHistoryIsSaved() {
        Task hotel = new Task("Отель", "Забронировать номер", Status.NEW);
        hotel.setTaskId(1);
        Task traveling = new Task("Путешествие", "Купить билеты", Status.IN_PROGRESS);
        traveling.setTaskId(2);

        historyManager.add(hotel);
        historyManager.add(traveling);

        List<Task> history = historyManager.getHistory();
        assertEquals(hotel, history.get(0));
        assertEquals(traveling, history.get(1));
    }

    // Добавление одной и той же задачи не должно дублировать её в истории.
    @Test
    void duplicateTasksNotAddedToHistory() {
        Task hotel = new Task("Отель", "Забронировать номер", Status.NEW);
        hotel.setTaskId(1);

        historyManager.add(hotel);
        historyManager.add(hotel);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
    }
}
