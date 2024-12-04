package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    /* так как требуется сравнение только по ID, переопределил метод Equals в классе Task, который сравнивает только ID,
    не знаю насколько это правильно */

    //  экземпляры класса Task равны друг другу, если равен их id;
    @Test
    void theTaskClassesEqualToEachOtherIfTheirIdIsEqual() {
        Task buyGas = new Task("Заправить машину", "Съездить на заправку", Status.NEW);
        Task callFriend = new Task("Позвонить другу", "Взять телефон", Status.NEW);
        buyGas.setTaskId(34);
        callFriend.setTaskId(34);

        assertEquals(buyGas, callFriend);
    }
}
