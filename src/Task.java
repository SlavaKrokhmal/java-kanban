import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private int id;
    private Status status;
    private TaskType type;

    public Task(String name, String description, Status status, TaskType type) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
    }

    public TaskType getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return getId() == task.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Задача{id=" + id + ", имя='" + name + '\'' +
                ", описание='" + description + '\'' +
                ", статус=" + status +
                '}';
    }
}
