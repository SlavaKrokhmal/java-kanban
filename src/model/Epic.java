package model;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Comparator;

public class Epic extends Task {
    private List<Subtask> subtasks;

    public Epic(String name, String description, Status status) {
        super(name, description, status, TaskType.EPIC, null, null);
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

    public void updateDurationAndTime() {
        if (subtasks.isEmpty()) {
            setStartTime(null);
            setDuration(null);
        } else {
            LocalDateTime minStart = subtasks.stream()
                    .filter(subtask -> subtask.getStartTime() != null)
                    .map(Subtask::getStartTime)
                    .min(LocalDateTime::compareTo)
                    .orElse(null);

            LocalDateTime maxEnd = subtasks.stream()
                    .filter(subtask -> subtask.getEndTime() != null)
                    .map(Subtask::getEndTime)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);

            if (minStart != null && maxEnd != null) {
                setStartTime(minStart);
                setDuration(Duration.between(minStart, maxEnd));
            } else {
                setStartTime(null);
                setDuration(null);
            }
        }
    }

    @Override
    public String toString() {
        String startTimeStr = (getStartTime() != null) ? getStartTime().toString() : "не установлено";
        String durationStr = (getDuration() != null) ? getDuration().toMinutes() + " минут" : "не установлено";
        return "Эпик{id=" + getId() +
                ", имя='" + getName() + '\'' +
                ", описание='" + getDescription() + '\'' +
                ", статус=" + getStatus() +
                ", количество подзадач=" + subtasks.size() +
                ", начало задачи=" + startTimeStr +
                ", продолжительность задачи=" + durationStr +
                '}';
    }
}
