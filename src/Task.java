public class Task {
    private String name;
    private String description;
    private int id;
    private Status status;

    public Task(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = Status.NEW;
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

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Задача{id=" + id + ", имя='" + name + '\'' +
                ", описание='" + description + '\'' +
                ", статус=" + status +
                '}';
    }
}