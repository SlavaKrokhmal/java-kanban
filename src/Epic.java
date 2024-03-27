import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtasks;

    public Epic(String name, String description, int status) {
        super(name, description, status);
        this.subtasks = new ArrayList<>();
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    @Override
    public void setStatus(Status status) {
        boolean allSubtasksDone = allSubtasksHaveStatus(Status.DONE);
        if (allSubtasksDone) {
            super.setStatus(Status.DONE);
        } else {
            super.setStatus(status);
        }
    }

    private boolean allSubtasksHaveStatus(Status status) {
        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() != status) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Эпик{id=").append(getId()).append(", имя='").append(getName()).append('\'');
        sb.append(", описание='").append(getDescription()).append('\'');
        sb.append(", статус=").append(getStatus());
        sb.append(", подзадача=").append(subtasks);
        sb.append('}');
        return sb.toString();
    }
}