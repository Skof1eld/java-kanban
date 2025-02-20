package logic;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    public T TaskManager;

    public abstract T createTaskManager();

    @BeforeEach
    public void setUp() {
        TaskManager = createTaskManager();
    }

    @Test
    public void addAndGetTask() {
        Task task = new Task("Task 1", "Description", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        TaskManager.addTask(task);
        Task retrievedTask = TaskManager.getTask(task.getTaskId());
        assertNotNull(retrievedTask);
        assertEquals(task, retrievedTask);
    }

    @Test
    public void addAndGetEpic() {
        Epic epic = new Epic("Epic 1", "Description");
        TaskManager.addEpic(epic);
        Epic retrievedEpic = TaskManager.getEpic(epic.getTaskId());
        assertNotNull(retrievedEpic);
        assertEquals(epic, retrievedEpic);
    }

    @Test
    public void addAndGetSubtask() {
        logic.TaskManager task = new InMemoryTaskManager();
        Epic epic = new Epic("Epic 1", "Description");
        task.addEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description",
                Status.NEW, Duration.ofMinutes(30), LocalDateTime.now(), epic.getTaskId());
        task.addSubtask(subtask);
        Subtask retrievedSubtask = task.getSubtask(subtask.getTaskId());
        assertNotNull(retrievedSubtask);
        assertEquals(subtask, retrievedSubtask);
    }

    @Test
    public void updateTaskStatus() {
        Task task = new Task("Task 1", "Description",
                Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        TaskManager.addTask(task);
        task.setStatus(Status.IN_PROGRESS);
        TaskManager.updateTask(task);
        assertEquals(Status.IN_PROGRESS, TaskManager.getTask(task.getTaskId()).getStatus());
    }

    @Test
    public void removeTask() {
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task("Task 1", "Description",
                Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.addTask(task);
        taskManager.removeTask(task.getTaskId());
        assertNull(taskManager.getTask(task.getTaskId()));
    }

    @Test
    public void removeEpic() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Epic 1", "Description");
        taskManager.addEpic(epic);
        taskManager.removeEpic(epic.getTaskId());
        assertNull(taskManager.getEpic(epic.getTaskId()));
    }

    @Test
    public void removeSubtask() {
        logic.TaskManager task = Managers.getDefault();
        Epic epic = new Epic("Epic 1", "Description");
        task.addEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description",
                Status.NEW, Duration.ofMinutes(30), LocalDateTime.now(), epic.getTaskId());
        task.addSubtask(subtask);
        task.removeSubtask(subtask.getTaskId());
        assertNull(task.getSubtask(subtask.getTaskId()));
    }

    @Test
    public void getHistory() {
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task("Task 1", "Description", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.addTask(task);
        taskManager.getTask(task.getTaskId());
        List<Task> history = taskManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task, history.getFirst());
    }

    @Test
    public void checkingIntersectionOfIntervals() {
        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("Task 1", "Description",
                Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        Task task2 = new Task("Task 2", "Description",
                Status.NEW, Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(15));
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        assertTrue(taskManager.overlappingTask(task1, task2));
    }

    // вычисление общей продолжительности эпика
    @Test
    public void getDurationForEpic() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Epic 1", "Description for Epic 1");
        taskManager.addEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description1",
                Status.NEW, Duration.ofHours(2), LocalDateTime.now(), epic.getTaskId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description2",
                Status.NEW, Duration.ofHours(3), LocalDateTime.now().plusHours(3), epic.getTaskId());
        Subtask subtask3 = new Subtask("Subtask 3", "Description3",
                Status.NEW, Duration.ofHours(1), LocalDateTime.now().plusHours(6), epic.getTaskId());

        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);

        Duration expectedDuration = Duration.ofHours(6);

        assertEquals(expectedDuration, taskManager.getDuration(epic));
    }

    //  время начала эпика должно быть равным самой ранней подзадачи
    @Test
    public void getStartTimeForEpic() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Epic 1", "Description Epic");
        taskManager.addEpic(epic);

        LocalDateTime now = LocalDateTime.now();
        Subtask subtask1 = new Subtask("Subtask 1", "Description1", Status.NEW,
                Duration.ofHours(2), now.plusHours(1), epic.getTaskId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description2", Status.NEW,
                Duration.ofHours(3), now.plusHours(2), epic.getTaskId());
        Subtask subtask3 = new Subtask("Subtask 3", "Description3", Status.NEW,
                Duration.ofHours(1), now, epic.getTaskId());

        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);

        LocalDateTime actualTime = taskManager.getStartTime(epic);
        assertEquals(now, actualTime);
    }

    //  время начала эпика должно быть равным самой поздней подзадачи
    @Test
    public void getEndTimeForEpic() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Epic 1", "Description Epic");
        taskManager.addEpic(epic);

        LocalDateTime now = LocalDateTime.now();
        Subtask subtask1 = new Subtask("Subtask 1", "Description1", Status.NEW,
                Duration.ofHours(2), now, epic.getTaskId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description2", Status.NEW,
                Duration.ofHours(3), now.plusHours(3), epic.getTaskId());
        Subtask subtask3 = new Subtask("Subtask 3", "Description3", Status.NEW,
                Duration.ZERO, now.plusHours(6), epic.getTaskId());    // самая поздняя подзадача

        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);

        LocalDateTime expectedTime = now.plusHours(6);

        taskManager.getEndTime(epic);
        LocalDateTime actualTime = epic.getEndTime();
        assertEquals(expectedTime, actualTime);
    }
}
