package model;

import logic.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Integer> subtaskList;
    private final InMemoryTaskManager taskManager = new InMemoryTaskManager();

    public Epic(String nameOfTheTask, String description) {
        super(nameOfTheTask, description, Status.NEW, Duration.ZERO, null);
        this.subtaskList = new ArrayList<>();
    }

    public void setSubtaskList(List<Integer> subtaskList) {
        this.subtaskList = subtaskList;
    }

    public List<Integer> getSubtaskList() {
        return subtaskList;
    }

    // вычисление общей продолжительности Epic задачи
    @Override
    public Duration getDuration() {
        Duration totalDuration = Duration.ZERO;
        for (Integer subtaskId : subtaskList) {
            Subtask subtask = getSubtaskById(subtaskId);
            if (subtask != null && subtask.getDuration() != null) {
                totalDuration = totalDuration.plus(subtask.getDuration());
            }
        }
        return totalDuration;
    }

    // определение времени завершения Epic задачи
    @Override
    public LocalDateTime getEndTime() {
        LocalDateTime lastResult = null;
        for (Integer subtaskId : subtaskList) {
            Subtask subtask = getSubtaskById(subtaskId);
            if (subtask != null) {
                LocalDateTime subtaskEnd = subtask.getEndTime();
                if (subtaskEnd != null && (lastResult == null || subtaskEnd.isAfter(lastResult))) {
                    lastResult = subtaskEnd;
                }
            }
        }
        return lastResult;
    }

    // определение времени начала Epic
    @Override
    public LocalDateTime getStartTime() {
        LocalDateTime earlieStart = null;
        for (Integer subtaskId : subtaskList) {
            Subtask subtask = getSubtaskById(subtaskId);
            if (subtask != null && subtask.getStartTime() != null) {
                if (earlieStart == null || subtask.getStartTime().isBefore(earlieStart)) {
                    earlieStart = subtask.getStartTime();
                }
            }
        }
        return earlieStart;
    }

    private Subtask getSubtaskById(int subtaskId) {
        return taskManager.getSubtask(subtaskId);
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
                '}';
    }
}
