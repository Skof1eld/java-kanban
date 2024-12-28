package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Integer> subtaskList;

    public Epic(String nameOfTheTask, String description) {
        super(nameOfTheTask, description, Status.NEW);
        this.subtaskList = new ArrayList<>();
    }

    public List<Integer> getSubtaskList() {
        return subtaskList;
    }

    public void setSubtaskList(List<Integer> subtaskList) {
        this.subtaskList = subtaskList;
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
                '}';
    }
}
