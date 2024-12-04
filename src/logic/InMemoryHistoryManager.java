package logic;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (history.size() == 10) {
            history.removeFirst();
        }
        history.add(copyTask(task));
    }

    // метод для сохранения истории задач в неизменном виде
    public Task copyTask(Task task) {
        if (task instanceof Subtask) {
            return new Subtask(task.getNameOfTheTask(), task.getDescription(), task.getStatus(), ((Subtask) task).getEpicId());
        } else if (task instanceof Epic) {
            return new Epic(task.getNameOfTheTask(), task.getDescription());
        } else {
            return new Task(task.getNameOfTheTask(), task.getDescription(), task.getStatus());
        }
    }

    @Override
    public ArrayList<Task> getHistory() {  // возвращаем список
        return new ArrayList<>(history);
    }
}
