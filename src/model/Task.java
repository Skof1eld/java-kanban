package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private static int counter = 0;
    private final String nameOfTheTask;
    private final String description;
    private Status status;
    private int taskId;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String nameOfTheTask, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.taskId = ++counter;
        this.nameOfTheTask = nameOfTheTask;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    // Вычисление времени завершения задачи
    public LocalDateTime getEndTime() {
        if (startTime == null || duration == null) {
            return null;
        }
        return startTime.plus(duration);
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
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }
}
