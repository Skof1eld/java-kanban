package logic;

import model.Task;

import java.util.List;

public interface HistoryManager {
    int MAX_HISTORY_SIZE = 10;

    void add(Task task);

    void remove(int id);

    List<Task> getHistory();
}
