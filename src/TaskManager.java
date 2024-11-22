import java.util.HashMap;

public class TaskManager {
    static int generalId = 1;
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();

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

        updateEpic(epic);
    }

    //  методы обновления
    public void updateTask(Task task) {
        tasks.put(task.getTaskId(), task);
    }

    public void updateEpic(Epic epic) {
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

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getTaskId(), subtask);

        Epic epic = epics.get(subtask.subtaskId());
        updateEpic(epic);

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
        subtasks.remove(id);
    }

    // методы удаления всех задач
    public void removeAllTask() {
        tasks.clear();

    }

    public void removeAllEpic() {
        for (Epic epic : epics.values()) {
            for (Integer subtask : epic.getSubtaskList()) {
                subtasks.remove(subtask);
            }
        }
        epics.clear();
    }

    public void removeAllSubtask(int id) {
        Epic epic = epics.get(id);
        for (Integer subtask : epic.getSubtaskList()) {
            subtasks.remove(subtask);
        }
        epic.getSubtaskList().clear();
    }

    // получение списка всех подзадач определенного epic
    public void printAllSubtaskOfTheOneEpic(int id) {
        Epic epic = epics.get(id);
        for (Integer subtask : epic.getSubtaskList()) {
            Subtask aSubtask = subtasks.get(subtask);
            System.out.println(aSubtask);
        }
    }

    // получение списка всех задач
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

