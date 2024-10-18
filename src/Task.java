import java.time.Duration;
import java.time.LocalDateTime;

public abstract class Task {
    private int id;
    private String title; // Убрали final
    private String description; // Убрали final
    private Status status; // Убрали final
    private final Duration duration;
    private LocalDateTime startTime;

    public Task(String title, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.id = -1; // По умолчанию id -1
        this.title = title;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) { // Добавили метод
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) { // Добавили метод
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) { // Добавили метод
        this.status = status;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public abstract TaskType getType();
}
