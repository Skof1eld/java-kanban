package logic;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    public T taskManager;

    public abstract T createTaskManager();

    @BeforeEach
    public void setUp() {
        taskManager = createTaskManager();
    }

    @Test
    public void addAndGetTask() {
        Task task = new Task("Task 1", "Description", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.addTask(task);
        Task retrievedTask = taskManager.getTask(task.getTaskId());
        assertNotNull(retrievedTask);
        assertEquals(task, retrievedTask);
    }

    @Test
    public void addAndGetEpic() {
        Epic epic = new Epic("Epic 1", "Description");
        taskManager.addEpic(epic);
        Epic retrievedEpic = taskManager.getEpic(epic.getTaskId());
        assertNotNull(retrievedEpic);
        assertEquals(epic, retrievedEpic);
    }

    @Test
    public void addAndGetSubtask() {
        TaskManager task = new InMemoryTaskManager();
        Epic epic = new Epic("Epic 1", "Description");
        task.addEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now(), epic.getTaskId());
        task.addSubtask(subtask);
        Subtask retrievedSubtask = task.getSubtask(subtask.getTaskId());
        assertNotNull(retrievedSubtask);
        assertEquals(subtask, retrievedSubtask);
    }

    @Test
    public void updateTaskStatus() {
        Task task = new Task("Task 1", "Description", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.addTask(task);
        task.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task);
        assertEquals(Status.IN_PROGRESS, taskManager.getTask(task.getTaskId()).getStatus());
    }

    @Test
    public void removeTask() {
        Task task = new Task("Task 1", "Description", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.addTask(task);
        taskManager.removeTask(task.getTaskId());
        assertNull(taskManager.getTask(task.getTaskId()));
    }

    @Test
    public void removeEpic() {
        Epic epic = new Epic("Epic 1", "Description");
        taskManager.addEpic(epic);
        taskManager.removeEpic(epic.getTaskId());
        assertNull(taskManager.getEpic(epic.getTaskId()));
    }

    @Test
    public void removeSubtask() {
        TaskManager task = new InMemoryTaskManager();
        Epic epic = new Epic("Epic 1", "Description");
        task.addEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now(), epic.getTaskId());
        task.addSubtask(subtask);
        task.removeSubtask(subtask.getTaskId());
        assertNull(task.getSubtask(subtask.getTaskId()));
    }

    @Test
    public void getHistory() {
        Task task = new Task("Task 1", "Description", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.addTask(task);
        taskManager.getTask(task.getTaskId());
        List<Task> history = taskManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }

    @Test
    public void checkingIntersectionOfIntervals() {
        Task task1 = new Task("Task 1", "Description", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        Task task2 = new Task("Task 2", "Description", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(15));
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        assertTrue(taskManager.overlappingTask(task1, task2));
    }
}
