package logic;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;

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

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<Subtask> getAllSubtasks();

    ArrayList<Subtask> getAllSubtasksOfOneEpic(int id);

    ArrayList<Task> getHistory();
}
