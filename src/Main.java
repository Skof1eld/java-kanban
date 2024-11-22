public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager taskManager = new TaskManager();

        Task buyGas = new Task("Заправить машину", "Съездить на заправку", Status.NEW);
        Task callFriend = new Task("Позвонить другу", "Взять телефон", Status.NEW);

        taskManager.addTask(buyGas);
        taskManager.addTask(callFriend);

        Epic traveling = new Epic("Путешествие", "Планирование путешествия");
        taskManager.addEpic(traveling);

        Subtask buyTickets = new Subtask("Покупка билетов", "Купить билеты у окна",
                Status.NEW, traveling.getTaskId());
        Subtask reserveHotel = new Subtask("Зарезервировать отель", "5 звезд, первая линия",
                Status.NEW, traveling.getTaskId());

        taskManager.addSubtask(buyTickets);
        taskManager.addSubtask(reserveHotel);

        Epic cleaner = new Epic("Убраться дома", "Убраться во всех комнатах");
        taskManager.addEpic(cleaner);

        Subtask vacuuming = new Subtask("Пропылесосить", "Вытереть пыль, помыть полы",
                Status.NEW, cleaner.getTaskId());

        taskManager.addSubtask(vacuuming);

        System.out.println(" !!! Все задачи на текущий момент !!! ");
        taskManager.printAllTypeTask();

        buyGas.setStatus(Status.DONE);
        callFriend.setStatus(Status.IN_PROGRESS);
        buyTickets.setStatus(Status.DONE);
        reserveHotel.setStatus(Status.IN_PROGRESS);
        vacuuming.setStatus(Status.IN_PROGRESS);


        taskManager.updateEpic(traveling);
        taskManager.updateEpic(cleaner);

        System.out.println(" -> Статусы после изменения <-");
        taskManager.printAllTypeTask();
        System.out.println("Traveling - " + traveling.getStatus());
        System.out.println("Cleaner - " + cleaner.getStatus());

        taskManager.removeTask(buyGas.getTaskId());
        taskManager.removeEpic(cleaner.getTaskId());

        System.out.println(" !!! Оставшиеся задачи после удаления !!! ");
        taskManager.printAllTypeTask();

    }
}

