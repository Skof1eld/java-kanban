package logic;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new ArrayList<>();

    // * добавил переменную в поле класса интерфейса, иначе 10 получается магическим числом, если правильно понимаю данное определение
    @Override
    public void add(Task task) {
        if (history.size() == MAX_HISTORY_SIZE) {
            history.removeFirst();
        }
        history.add(copyTask(task));
    }

    // метод для сохранения истории задач в неизменном виде
    public Task copyTask(Task task) {
        if (task instanceof Subtask) {
            return new Subtask(task.getNameOfTheTask(), task.getDescription(), task.getStatus(), ((Subtask) task).getEpicId());
        } else if (task instanceof Epic) {
            Epic epicCopy = new Epic(task.getNameOfTheTask(), task.getDescription());
            epicCopy.setTaskId(task.getTaskId());
            return epicCopy;
        } else {
            Task taskCopy = new Task(task.getNameOfTheTask(), task.getDescription(), task.getStatus());
            taskCopy.setTaskId(task.getTaskId());
            return taskCopy;
        }
    }

    @Override
    public List<Task> getHistory() {  // возвращаем список
        return history;
    }
}
