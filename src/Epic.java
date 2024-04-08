import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtasks;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        this.subtasks = new ArrayList<>();
    }

    public void addSubtask(Subtask subtask) {
        boolean isAddingSelf = this.getId() == subtask.getEpicId();
        if (isAddingSelf) {
            throw new IllegalArgumentException("Эпик не может быть добавлен в себя");
        }
        subtasks.add(subtask);
    }

    public void removeSubtask(Subtask subtask) {
        subtasks.remove(subtask);
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void clearSubtasks() {
        subtasks.clear();
    }

    @Override
    public String toString() {
        return "Эпик{id=" + getId() + ", имя='" + getName() + '\'' +
                ", описание='" + getDescription() + '\'' +
                ", статус=" + getStatus() +
                ", количество подзадач=" + subtasks.size() +
                '}';
    }
}