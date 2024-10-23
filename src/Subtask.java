import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private final Integer epicId; // Изменяем на объектный тип Integer

    public Subtask(String title, String description, Status status, Duration duration,
                   LocalDateTime startTime, Integer epicId) {
        super(title, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.Subtask;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", duration=" + getDuration().toMinutes() + " minutes" +
                ", startTime=" + getStartTime() +
                ", epicId=" + epicId +
                '}';
    }
}
