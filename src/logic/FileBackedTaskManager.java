package logic;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        save();
    }

    // Сохраняем все задачи, эпики, подзадачи в файл в виде строки
    protected void save() {
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
            writer.write("id,type,name,status,description,epic\n");

            for (Task task : getAllTasks()) {
                writer.write(taskToString(task) + "\n");
            }

            for (Epic epic : getAllEpics()) {
                writer.write(taskToString(epic) + "\n");
            }

            for (Subtask subtask : getAllSubtasks()) {
                writer.write(taskToString(subtask) + "\n");
            }

        } catch (IOException e) {
            throw new ManagerSaveException("!Ошибка сохранения в файл!", e);
        }
    }

    // Сохранение задачи в строку
    private String taskToString(Task task) {
        if (task instanceof Epic epic) {
            return epic.getTaskId() + ",EPIC," + epic.getNameOfTheTask() + "," + epic.getStatus() + "," +
                    epic.getDescription() + ",";
        } else if (task instanceof Subtask subtask) {
            return subtask.getTaskId() + ",SUBTASK," + subtask.getNameOfTheTask() + "," + subtask.getStatus() + "," +
                    subtask.getDescription() + "," + subtask.getEpicId();
        } else {
            return task.getTaskId() + ",TASK," + task.getNameOfTheTask() + "," + task.getStatus() + "," +
                    task.getDescription() + ",";
        }
    }

    // Создание задачи из строки
    private void taskFromString(File file) {
        try {
            List<String> lines = Files.readAllLines(file.toPath());

            if (lines.isEmpty()) {
                return;
            }

            /*
             наткнулся на ошибку чтения из файла первой строки ("id,type,name,status,description,epic\n" - что логично),
             на не корректную ее обработку, поэтому пропустил её чтение
             */
            for (String line : lines.subList(1, lines.size())) {
                String[] fields = line.split(",");
                if (fields.length < 5) continue;

                int id = Integer.parseInt(fields[0]);
                String type = fields[1];
                String name = fields[2];
                Status status = Status.valueOf(fields[3]);
                String description = fields[4];
                int epicId = 0;
                if (fields.length > 5) {
                    epicId = Integer.parseInt(fields[5]);
                }

                Task task;
                switch (type) {
                    case "TASK":
                        task = new Task(name, description, status);
                        task.setTaskId(id);
                        addTask(task);
                        break;
                    case "EPIC":
                        Epic epic = new Epic(name, description);
                        epic.setTaskId(id);
                        epic.setStatus(status);
                        addEpic(epic);
                        break;
                    case "SUBTASK":
                        Subtask subtask = new Subtask(name, description, status, epicId);
                        subtask.setTaskId(id);
                        addSubtask(subtask);
                        break;
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("!Ошибка загрузки данных из файла!", e);
        }
    }

    // Восстанавливает данные менеджера из файла
    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.taskFromString(file);
        return manager;
    }

    // Cобственное непроверяемое исключение ManagerSaveException
    public static class ManagerSaveException extends RuntimeException {
        public ManagerSaveException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
