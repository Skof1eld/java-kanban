package model;

import java.util.Objects;

public class Subtask extends Task {
    private int getEpicId;

    public Subtask(String nameOfTheTask, String description, Status status, int getEpicId) {
        super(nameOfTheTask, description, status);
        this.getEpicId = getEpicId;

    }

    public int getEpicId() {
        return getEpicId;
    }

    public void setEpicId(int getEpicId) {
        this.getEpicId = getEpicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return getEpicId == subtask.getEpicId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getEpicId);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "subtaskId=" + getEpicId +
                ", nameOfTheTask='" + getNameOfTheTask() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", taskId=" + getTaskId() +
                '}';
    }
}
