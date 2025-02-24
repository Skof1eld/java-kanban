package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static model.TypeTask.EPIC;

public class Epic extends Task {
    private List<Integer> subtaskList;
    private LocalDateTime endTime;

    public Epic(String nameOfTheTask, String description) {
        super(nameOfTheTask, description, Status.NEW, Duration.ZERO, null);
        this.subtaskList = new ArrayList<>();
        this.endTime = null;
    }

    public void setSubtaskList(List<Integer> subtaskList) {
        this.subtaskList = subtaskList;
    }

    public List<Integer> getSubtaskList() {
        return subtaskList;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(subtaskList);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "nameOfTheTask='" + getNameOfTheTask() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", taskId=" + getTaskId() +
                ", subtaskList=" + subtaskList +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                ", endTime=" + getEndTime() +
                ", type=" + EPIC +
                '}';
    }
}
