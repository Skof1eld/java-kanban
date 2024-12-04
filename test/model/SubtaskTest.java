package model;

import logic.InMemoryTaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    /* так как требуется сравнение только по ID, переопределил метод Equals в классе Task, который сравнивает только ID,
       не знаю насколько это правильно */

    // наследники класса Task (Subtask) равны друг другу, если равен их id;
    @Test
    void theSubTaskClassesEqualToEachOtherIfTheirIdIsEqual() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Epic traveling = new Epic("Путешествие", "Планирование путешествия");
        taskManager.addEpic(traveling);

        Subtask buyTickets = new Subtask("Покупка билетов", "Купить билеты у окна",
                Status.NEW, traveling.getTaskId());
        Subtask reserveHotel = new Subtask("Зарезервировать отель", "5 звезд, первая линия",
                Status.NEW, traveling.getTaskId());
        buyTickets.setEpicId(21);
        reserveHotel.setEpicId(21);

        assertEquals(buyTickets, reserveHotel);
    }

    // проверяем, что при одинаковых id задачи и подзадачи, они не равны, подзадача не может быть эпиком и ссылаться сама на себя
    @Test
    void subtaskCannotBeEpicAndReferToItself() {
        Subtask reserveHotel = new Subtask("Отель", "первая линия", Status.NEW, 34);

        reserveHotel.setEpicId(34);
        assertNotEquals(reserveHotel.getTaskId(), reserveHotel.getEpicId());
    }
}