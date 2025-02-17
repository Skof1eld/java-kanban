package model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    //  экземпляры класса Task равны друг другу, если равен их id;
    @Test
    void theTaskClassesEqualToEachOtherIfTheirIdIsEqual() {
        Task buyGas = new Task("Заправить машину", "Съездить на заправку",
                Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        Task callFriend = new Task("Позвонить другу", "Взять телефон",
                Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        buyGas.setTaskId(34);
        callFriend.setTaskId(34);

        assertEquals(buyGas, callFriend);
    }
}
