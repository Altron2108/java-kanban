import com.google.gson.annotations.Expose;

import java.time.Duration;
import java.time.LocalDateTime;

public class RegularTask extends Task {
    @Expose
    private final Duration taskDuration; // Переименованное поле
    @Expose
    private final LocalDateTime taskStartTime; // Переименованное поле

    public RegularTask(String title, String description, Status status, Duration duration, LocalDateTime startTime) {
        super(title, description, status, duration, startTime);
        this.taskDuration = duration; // Устанавливаем длительность задачи
        this.taskStartTime = startTime; // Устанавливаем время начала задачи
    }

    public Duration getTaskDuration() {
        return taskDuration; // Возвращаем длительность задачи
    }

    public LocalDateTime getTaskStartTime() {
        return taskStartTime; // Возвращаем время начала задачи
    }

    @Override
    public TaskType getType() {
        return TaskType.Task; // Возвращаем тип задачи
    }

    @Override
    public String toString() {
        return "RegularTask{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", duration=" + taskDuration.toMinutes() + " minutes" +
                ", startTime=" + taskStartTime +
                '}';
    }
}
