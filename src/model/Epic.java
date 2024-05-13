package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtasks;

    public Epic(String name, String description, Status status) {
        super(name, description, status, TaskType.EPIC);
        this.subtasks = new ArrayList<>();
    }

    public void addSubtask(Subtask subtask) {
        if (this.getId() != subtask.getEpicId()) {
            throw new IllegalArgumentException("Подзадача не соответствует ID этого эпика");
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
