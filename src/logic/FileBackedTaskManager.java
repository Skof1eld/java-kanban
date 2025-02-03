package logic;

import model.*;

import java.io.*;
import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;
    private static final String HEADER = "id,type,name,status,description,epic\n";

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

    @Override
    public void removeAllTask() {
        super.removeAllTask();
        save();
    }

    @Override
    public void removeAllEpic() {
        super.removeAllEpic();
        save();
    }

    @Override
    public void removeAllSubtask() {
        super.removeAllSubtask();
        save();
    }

    // Сохраняем все задачи, эпики, подзадачи в файл в виде строки
    protected void save() {
        String lineSeparator = System.lineSeparator();

        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
            writer.write(HEADER);

            for (Task task : getAllTasks()) {
                writer.write(taskToString(task) + lineSeparator);
            }

            for (Epic epic : getAllEpics()) {
                writer.write(taskToString(epic) + lineSeparator);
            }

            for (Subtask subtask : getAllSubtasks()) {
                writer.write(taskToString(subtask) + lineSeparator);
            }

        } catch (IOException e) {
            throw new ManagerSaveException("!Ошибка сохранения в файл!", e);
        }
    }

    // Сохранение задачи в строку
    private String taskToString(Task task) {
        if (task instanceof Epic epic) {
            return String.format("%d,EPIC,%s,%s,%s,", epic.getTaskId(), epic.getNameOfTheTask(), epic.getStatus(),
                    epic.getDescription());
        } else if (task instanceof Subtask subtask) {
            return String.format("%d,SUBTASK,%s,%s,%s,%d", subtask.getTaskId(), subtask.getNameOfTheTask(),
                    subtask.getStatus(), subtask.getDescription(), subtask.getEpicId());
        } else {
            return String.format("%d,TASK,%s,%s,%s,", task.getTaskId(), task.getNameOfTheTask(), task.getStatus(),
                    task.getDescription());
        }
    }

    // Создание задачи из строки
    private void taskFromString(File file) {
        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            String line = reader.readLine();
            if (line == null) {
                return;
            }

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length < 5) {
                    throw new IllegalArgumentException("!Формат строки " + line + " не верен!");
                }

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
                TypeTask typeTask = TypeTask.valueOf(type);
                switch (typeTask) {
                    case TASK:
                        task = new Task(name, description, status);
                        task.setTaskId(id);
                        addTask(task);
                        break;
                    case EPIC:
                        Epic epic = new Epic(name, description);
                        epic.setTaskId(id);
                        epic.setStatus(status);
                        addEpic(epic);
                        break;
                    case SUBTASK:
                        Subtask subtask = new Subtask(name, description, status, epicId);
                        subtask.setTaskId(id);
                        addSubtask(subtask);
                        break;
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("!Ошибка загрузки данных из файла!", e);
        } catch (IllegalArgumentException e) {
            throw new ManagerSaveException(e.getMessage(), e); // * обернул в свое исключение
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
