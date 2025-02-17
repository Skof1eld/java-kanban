package logic;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {
    private final InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    public void emptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    public void addAndRemoveFromHistory() {
        Task task1 = new Task("Task 1", "Description",
                Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        Task task2 = new Task("Task 2", "Description",
                Status.NEW, Duration.ofMinutes(30), LocalDateTime.now().plusHours(1));
        historyManager.add(task1);
        historyManager.add(task2);

        // Проверяем, что задачи добавлены
        assertEquals(2, historyManager.getHistory().size());

        // Удаление из начала
        historyManager.remove(task1.getTaskId());
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task2, history.get(0));

        // Удаление из середины/конца
        historyManager.add(task1);
        historyManager.remove(task2.getTaskId());
        history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task1, history.get(0));
    }

    @Test
    public void duplicateTasksInHistory() {
        Task task = new Task("Task 1", "Description",
                Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        historyManager.add(task);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size());
    }
}
