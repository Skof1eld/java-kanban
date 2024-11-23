package logic;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    int generalId = 1;
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    //  методы добавления
    public void addTask(Task task) {
        task.setTaskId(generalId++);
        tasks.put(task.getTaskId(), task);
    }

    public void addEpic(Epic epic) {
        epic.setTaskId(generalId++);
        epics.put(epic.getTaskId(), epic);
    }

    public void addSubtask(Subtask subtask) {
        subtask.setTaskId(generalId++);
        subtasks.put(subtask.getTaskId(), subtask);

        Epic epic = epics.get(subtask.subtaskId());

        if (epic == null) {
            System.out.println(" !!! Ошибка: Epic не найден !!!");
            return;
        }
        epic.getSubtaskList().add(subtask.getTaskId());
        updateStatusEpic(epic);
    }

    //  методы обновления
    public void updateTask(Task task) {
        tasks.put(task.getTaskId(), task);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getTaskId(), epic);
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getTaskId(), subtask);
        Epic epic = epics.get(subtask.subtaskId());
        updateStatusEpic(epic);
    }

    public void updateStatusEpic(Epic epic) {
        boolean statusNew = true;
        boolean statusDone = true;

        for (Integer subtaskId : epic.getSubtaskList()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask != null) {
                if (subtask.getStatus() != Status.NEW) {
                    statusNew = false;
                }
                if (subtask.getStatus() != Status.DONE) {
                    statusDone = false;
                }
            }
        }

        if (epic.getSubtaskList().isEmpty() || statusNew) {
            epic.setStatus(Status.NEW);
        } else if (statusDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    //  методы получение по идентификатору
    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    // методы удаления по идентификатору
    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void removeEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtaskList()) {
                subtasks.remove(subtaskId);
            }
        }
        epics.remove(id);
    }

    public void removeSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.subtaskId());
        epic.getSubtaskList().remove((Integer) id);
        subtasks.remove(id);
        updateStatusEpic(epic);
    }

    // методы удаления всех задач
    public void removeAllTask() {
        tasks.clear();
    }

    public void removeAllEpic() {
        subtasks.clear();
        epics.clear();
    }

    public void removeAllSubtask() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtaskList().clear();
            updateStatusEpic(epic);
        }
    }

    // получение списка всех задач, подзадач, эпиков
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // получение списка всех подзадач определенного Epic
    public ArrayList<Subtask> getAllSubtasksOfOneEpic(int id) {
        ArrayList<Subtask> listSubtask = new ArrayList<>();
        Epic epic = epics.get(id);
        for (Integer subtaskIndex : epic.getSubtaskList()) {
            Subtask subtask = subtasks.get(subtaskIndex);
            listSubtask.add(subtask);
        }
        return listSubtask;
    }

    // * Методы печати для проверки
    public void printAllSubtaskOfTheOneEpic(int id) {
        Epic epic = epics.get(id);
        for (Integer subtask : epic.getSubtaskList()) {
            Subtask aSubtask = subtasks.get(subtask);
            System.out.println(aSubtask);
        }
    }

    public void printAllTypeTask() {
        System.out.println(" -> Список всех Task <-");
        for (Task task : tasks.values()) {
            System.out.println(task);
        }
        System.out.println(" -> Список всех Epic <-");
        for (Epic epic : epics.values()) {
            System.out.println(epic.toString());
        }
        System.out.println(" -> Список всех Subtask <-");
        for (Subtask subtask : subtasks.values()) {
            System.out.println(subtask);
        }
    }
}
