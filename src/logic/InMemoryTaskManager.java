package logic;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int generalId = 1;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = new InMemoryHistoryManager();

    // * сортируем задачи по времени начала и добавляем задачи без startTime в конец списка
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder())));

    //  методы добавления
    @Override
    public void addTask(Task task) {
        // проверяем, не пересекается ли добавляемая задача с уже существующими
        boolean hasOverlap = tasks.values().stream().anyMatch(existingTask -> overlappingTask(task, existingTask));

        if (hasOverlap) {
            System.out.println(" !!! Error: Задача пересекается с другой задачей по времени !!!");
            return;
        }
        task.setTaskId(generalId++);
        tasks.put(task.getTaskId(), task);
        prioritizedTasks.add(task);
        historyManager.add(task);
    }

    @Override
    public void addEpic(Epic epic) {
        // проверяем пересечение эпика с задачами и подзадачами
        boolean hasOverlap = epics.values().stream().anyMatch(existingEpic -> overlappingTask(epic, existingEpic)) ||
                tasks.values().stream().anyMatch(existingTask -> overlappingTask(epic, existingTask)) ||
                subtasks.values().stream().anyMatch(existingSubtask -> overlappingTask(epic, existingSubtask));

        if (hasOverlap) {
            System.out.println(" !!! Error: Эпик пересекается с другой задачей или подзадачей по времени !!!");
            return;
        }
        epic.setTaskId(generalId++);
        epics.put(epic.getTaskId(), epic);
        prioritizedTasks.add(epic);
    }

    // Метод для добавления подзадачи
    @Override
    public void addSubtask(Subtask subtask) {
        // проверяем пересечение подзадачи с задачами и эпиками
        boolean hasOverlap = tasks.values().stream().anyMatch(existingTask -> overlappingTask(subtask, existingTask)) ||
                epics.values().stream().anyMatch(existingEpic -> overlappingTask(subtask, existingEpic)) ||
                subtasks.values().stream().anyMatch(existingSubtask -> overlappingTask(subtask, existingSubtask));

        if (hasOverlap) {
            System.out.println(" !!! Error: Подзадача пересекается с другой задачей или подзадачей по времени !!!");
            return;
        }
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
        prioritizedTasks.add(subtask);
        updateStatusEpic(epic);
    }

    //  методы обновления
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getTaskId(), task);
        prioritizedTasks.add(task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getTaskId(), epic);
        prioritizedTasks.add(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getTaskId(), subtask);
        prioritizedTasks.add(subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            updateStatusEpic(epic);
        }
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

    // метод удаления задачи из списка истории
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
        Task task = tasks.remove(id);
        if (task != null) {
            prioritizedTasks.remove(task);
        }
        removeHistory(id);
    }

    @Override
    public void removeEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtaskList()) {
                Subtask subtask = subtasks.remove(subtaskId);
                if (subtask != null) {
                    prioritizedTasks.remove(subtask);
                    removeHistory(subtaskId);
                }
            }
            prioritizedTasks.remove(epic);
        }
        removeHistory(id);
    }

    @Override
    public void removeSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            prioritizedTasks.remove(subtask);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.getSubtaskList().remove((Integer) id);
                updateStatusEpic(epic);
            }
        }
        removeHistory(id);
    }

    // методы удаления всех задач
    @Override
    public void removeAllTask() {
        for (Integer idTask : tasks.keySet()) {
            removeHistory(idTask); // * удаление из истории
        }
        tasks.clear();
    }

    @Override
    public void removeAllEpic() {   // * для эпиков придется удалять и все подзадачи, связанные с этим эпиком
        for (Integer idEpic : epics.keySet()) {
            Epic epic = epics.get(idEpic);
            if (epic != null) {
                for (Integer idSubtask : epic.getSubtaskList()) {
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
        for (Integer idSubtask : tasks.keySet()) {
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
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // получение списка всех подзадач определенного Epic
    @Override
    public List<Subtask> getAllSubtasksOfOneEpic(int id) {
        List<Subtask> listSubtask = new ArrayList<>();
        Epic epic = epics.get(id);
        for (Integer subtaskIndex : epic.getSubtaskList()) {
            Subtask subtask = subtasks.get(subtaskIndex);
            listSubtask.add(subtask);
        }
        return listSubtask;
    }

    // Методы печати для проверки
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

    // метод проверки пересекаются ли две задачи по времени
    @Override
    public boolean overlappingTask(Task task1, Task task2) {

        if (task1.getStartTime() == null || task2.getStartTime() == null) {
            return false;
        }

        LocalDateTime task1Start = task1.getStartTime();
        LocalDateTime task1End = task1.getEndTime() != null ? task1.getEndTime() : task1Start.plus(task1.getDuration());
        LocalDateTime task2Start = task2.getStartTime();
        LocalDateTime task2End = task2.getEndTime() != null ? task2.getEndTime() : task2Start.plus(task2.getDuration());
        return (task1Start.isBefore(task2End) && task1End.isAfter(task2Start));
    }

    // метод сортировки задач по времени начала
    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    // вычисление общей продолжительности Epic задачи
    @Override
    public Duration getDuration(Epic epic) {
        Duration totalDuration = Duration.ZERO;
        for (Integer subtaskId : epic.getSubtaskList()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask != null && subtask.getDuration() != null) {
                totalDuration = totalDuration.plus(subtask.getDuration());
            }
        }
        return totalDuration;
    }

    // определение времени начала задачи
    @Override
    public LocalDateTime getStartTime(Epic epic) {
        LocalDateTime earliestStart = null;
        for (Integer subtaskId : epic.getSubtaskList()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask != null && subtask.getStartTime() != null) {
                if (earliestStart == null || subtask.getStartTime().isBefore(earliestStart)) {
                    earliestStart = subtask.getStartTime();
                }
            }
        }
        return earliestStart;
    }

    // определение времени завершения задачи
    @Override
    public void getEndTime(Epic epic) {
        LocalDateTime lastEnd = null;
        for (Integer subtaskId : epic.getSubtaskList()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask != null) {
                LocalDateTime subtaskEnd = subtask.getEndTime();
                if (subtaskEnd != null && (lastEnd == null || subtaskEnd.isAfter(lastEnd))) {
                    lastEnd = subtaskEnd;
                }
            }
        }
        epic.setEndTime(lastEnd);
    }
}
