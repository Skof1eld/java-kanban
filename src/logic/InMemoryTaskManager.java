package logic;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int generalId = 1;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = new InMemoryHistoryManager();

    //  методы добавления
    @Override
    public void addTask(Task task) {
        task.setTaskId(generalId++);
        tasks.put(task.getTaskId(), task);
        historyManager.add(task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setTaskId(generalId++);
        epics.put(epic.getTaskId(), epic);
    }

    // Метод для добавления подзадачи
    @Override
    public void addSubtask(Subtask subtask) {
        subtask.setTaskId(generalId++);

        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            System.out.println(" !!! Error: Epic не найден !!!");
            return;
        }
        if (epic.getTaskId() == subtask.getTaskId()) {
            System.out.println(" !!! Error: Epic нельзя добавить в самого себя в виде подзадачи !!!");
            return;
        }
        epic.getSubtaskList().add(subtask.getTaskId());
        subtasks.put(subtask.getTaskId(), subtask);
        updateStatusEpic(epic);
    }

    //  методы обновления
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getTaskId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getTaskId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getTaskId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        updateStatusEpic(epic);
    }

    @Override
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
    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        historyManager.add(task); // добавление задачи в список history
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic); // добавление эпика в список history
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);  // добавление подзадачи в список history
        return subtask;
    }

    // получение списка истории просмотров
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    /* Если я правильно понял замечание, то лучше создать отдельный метод по удалению задачи из списка истории */
    public void removeHistory(int id) {
        List<Task> history = historyManager.getHistory();
        for (int i = 0; i < history.size(); i++) {
            Task task = history.get(i);
            if (task != null && task.getTaskId() == id) {
                historyManager.getHistory().remove(i);
                return;
            }
        }
    }

    // методы удаления по идентификатору
    @Override
    public void removeTask(int id) {
        tasks.remove(id);
        removeHistory(id);  // * удаление из истории
    }

    @Override
    public void removeEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtaskList()) {
                subtasks.remove(subtaskId);
                removeHistory(subtaskId);  // * удаление из истории
            }
        }
        epics.remove(id);
    }

    @Override
    public void removeSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubtaskList().remove((Integer) id);
        subtasks.remove(id);
        updateStatusEpic(epic);
        removeHistory(id); // * удаление из истории
    }

    // методы удаления всех задач
    @Override
    public void removeAllTask() {
        for(Integer idTask : tasks.keySet()) {
            removeHistory(idTask); // * удаление из истории
        }
        tasks.clear();
    }

    @Override
    public void removeAllEpic() {   // * для эпиков придется удалять и все подзадачи, связанные с этим эпиком
        for (Integer idEpic : epics.keySet()) {
            Epic epic = epics.get(idEpic);
            if(epic != null) {
                for(Integer idSubtask : epic.getSubtaskList()) {
                    removeHistory(idSubtask); // * удаление из истории подзадачи
                }
            }
            removeHistory(idEpic);  // * удаление из истории Epic
        }
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void removeAllSubtask() {
        for(Integer idSubtask : tasks.keySet()) {
            removeHistory(idSubtask); // * удаление из истории
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtaskList().clear();
            updateStatusEpic(epic);
        }
    }

    // получение списка всех задач, подзадач, эпиков
    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // получение списка всех подзадач определенного Epic
    @Override
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
