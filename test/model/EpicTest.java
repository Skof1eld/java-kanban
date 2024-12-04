package model;

import logic.InMemoryTaskManager;
import logic.Managers;
import logic.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    /* так как требуется сравнение только по ID, переопределил метод Equals в классе Task, который сравнивает только ID,
         не знаю насколько это правильно */

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
        Subtask subtask = new Subtask("Покупка билетов", "Купить билеты у окна", Status.NEW, epic.getTaskId());
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);

        assertEquals(0, epic.getSubtaskList().size());
    }
}