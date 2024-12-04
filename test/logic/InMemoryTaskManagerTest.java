package logic;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    // InMemoryTaskManager добавляет задачи разного типа и может найти их по id;
    @Test
    void canAddTasksDifferentTypesAndFindThemById() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Task task = new Task("Банкомат", "Снять деньги", Status.NEW);
        Epic epic = new Epic("Путешествие", "Планирование путешествия");
        taskManager.addTask(task);
        taskManager.addEpic(epic);

        Subtask subtask = new Subtask("Покупка билетов", "Купить билеты у окна", Status.NEW, epic.getTaskId());
        taskManager.addSubtask(subtask);

        Task taskActual = taskManager.getTask(task.getTaskId());
        Epic epicActual = taskManager.getEpic(epic.getTaskId());
        Subtask subtaskActual = taskManager.getSubtask(subtask.getTaskId());

        assertNotNull(taskActual);
        assertEquals(task, taskActual);

        assertNotNull(epicActual);
        assertEquals(epic, epicActual);

        assertNotNull(subtaskActual);
        assertEquals(subtask, subtaskActual);
    }
}
