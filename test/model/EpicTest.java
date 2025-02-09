package model;

import logic.InMemoryTaskManager;
import logic.Managers;
import logic.TaskManager;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    // наследники класса Task (Epic) равны друг другу, если равен их id;
    @Test
    void theEpicClassesEqualToEachOtherIfTheirIdIsEqual() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Epic traveling = new Epic("Путешествие", "Планирование путешествия");
        taskManager.addEpic(traveling);
        Epic cleaner = new Epic("Убраться дома", "Убраться во всех комнатах");
        taskManager.addEpic(cleaner);
        traveling.setTaskId(11);
        cleaner.setTaskId(11);

        assertEquals(traveling, cleaner);
    }

    // объект Epic нельзя добавить в самого себя в виде подзадачи
    @Test
    void epicCannotBeAddedToItselfAsSubtask() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Путешествие", "Планирование путешествия");
        Subtask subtask = new Subtask("Покупка билетов", "Купить билеты у окна", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.now(), epic.getTaskId());
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);

        assertEquals(0, epic.getSubtaskList().size());
    }

    // расчёт статуса Epic
    @Test
    public void calculatingEpicStatus() {
        TaskManager task = new InMemoryTaskManager();
        Epic epic = new Epic("Epic 1", "Description");
        task.addEpic(epic);

        // подзадачи со статусом NEW
        Subtask subtask1 = new Subtask("Subtask 1", "Description", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.now(), epic.getTaskId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.now().plusHours(1), epic.getTaskId());
        task.addSubtask(subtask1);
        task.addSubtask(subtask2);
        assertEquals(Status.NEW, epic.getStatus());

        subtask1.setStatus(Status.DONE);  // подзадачи со статусом DONE
        subtask2.setStatus(Status.DONE);
        task.updateSubtask(subtask1);
        task.updateSubtask(subtask2);
        assertEquals(Status.DONE, epic.getStatus());

        subtask1.setStatus(Status.NEW);  // подзадачи со статусами NEW и DONE
        task.updateSubtask(subtask1);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());

        subtask1.setStatus(Status.IN_PROGRESS);  // подзадачи со статусом IN_PROGRESS
        task.updateSubtask(subtask1);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }
}
