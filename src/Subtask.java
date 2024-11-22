import java.util.Objects;

public class Subtask extends Task {
    private int subtaskId;

    public Subtask(String nameOfTheTask, String description, Status status, int subtaskId) {
        super(nameOfTheTask, description, status);
        this.subtaskId = subtaskId;
    }

    public int subtaskId() {
        return subtaskId;
    }

    public void setSubtaskId(int subtaskId) {
        this.subtaskId = subtaskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return subtaskId == subtask.subtaskId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(subtaskId);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "subtaskId=" + subtaskId +
                ", nameOfTheTask='" + getNameOfTheTask() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", taskId=" + getTaskId() +
                '}';
    }
}

