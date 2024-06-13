package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, Status status, int epicId, LocalDateTime startTime, Duration duration) {
        super(name, description, status, TaskType.SUBTASK, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        String startTimeStr = (getStartTime() != null) ? getStartTime().toString() : "не установлено";
        String durationStr = (getDuration() != null) ? getDuration().toMinutes() + " минут" : "не установлено";
        return "Подзадача{id=" + getId() +
                ", имя='" + getName() + '\'' +
                ", описание='" + getDescription() + '\'' +
                ", статус=" + getStatus() +
                ", id эпика=" + epicId +
                ", начало задачи=" + startTimeStr +
                ", продолжительность задачи=" + durationStr +
                '}';
    }
}
