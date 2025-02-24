package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String nameOfTheTask, String description, Status status, Duration duration, LocalDateTime startTime, int getEpicId) {
        super(nameOfTheTask, description, status, duration, startTime);
        this.epicId = getEpicId;

    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int getEpicId) {
        this.epicId = getEpicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(epicId);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", nameOfTheTask='" + getNameOfTheTask() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", taskId=" + getTaskId() +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                '}';
    }
}
