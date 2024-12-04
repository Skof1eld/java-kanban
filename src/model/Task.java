package model;

import java.util.Objects;

public class Task {
    private final String nameOfTheTask;
    private final String description;
    private Status status;
    private int taskId;

    public Task(String nameOfTheTask, String description, Status status) {
        this.nameOfTheTask = nameOfTheTask;
        this.description = description;
        this.status = status;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public String getNameOfTheTask() {
        return nameOfTheTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameOfTheTask, description, status, taskId);
    }

    @Override
    public String toString() {
        return "Task{" +
                "nameOfTheTask='" + nameOfTheTask + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", taskId=" + taskId +
                '}';
    }
}
