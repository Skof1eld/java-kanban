package logic;

import static org.junit.jupiter.api.Assertions.*;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;

public class TaskManagerTest {

    // проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    void checkTheImmutabilityTaskWhenAddingTaskToManager() {
        TaskManager taskManager = Managers.getDefault();

        Task task = new Task("Отель", "Первая линия", Status.NEW);
        String currentName = task.getNameOfTheTask();
        String currentDescription = task.getDescription();
        Status currentStatus = task.getStatus();
        taskManager.addTask(task);

        Task taskActual = taskManager.getTask(task.getTaskId());
        assertEquals(currentName, taskActual.getNameOfTheTask());
        assertEquals(currentDescription, taskActual.getDescription());
        assertEquals(currentStatus, taskActual.getStatus());
    }

    //проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера
    @Test
    void tasksWithGivenIdAndGeneratedIdDoNotConflictInManager() {
        TaskManager taskManager = Managers.getDefault();

        Task taskStatId = new Task("taskStatId", "message", Status.NEW);
        taskStatId.setTaskId(34);
        taskManager.addTask(taskStatId);

        Task taskGenerateId = new Task("taskGenerateId", "new message", Status.NEW);
        taskManager.addTask(taskGenerateId);

        assertNotEquals(taskStatId.getTaskId(), taskGenerateId.getTaskId());
    }
}