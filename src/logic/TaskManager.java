package logic;

import model.Epic;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public interface TaskManager {

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void updateStatusEpic(Epic epic);

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    void removeTask(int id);

    void removeEpic(int id);

    void removeSubtask(int id);

    void removeAllTask();

    void removeAllEpic();

    void removeAllSubtask();

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    List<Subtask> getAllSubtasksOfOneEpic(int id);

    List<Task> getHistory();

    boolean overlappingTask(Task task1, Task task2);

    List<Task> getPrioritizedTasks();

    Duration getDuration(Epic epic);

    LocalDateTime getStartTime(Epic epic);

    void getEndTime(Epic epic);
}
