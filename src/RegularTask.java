import java.time.Duration;
import java.time.LocalDateTime;

public class RegularTask extends Task {

    // Конструктор с параметрами для инициализации задачи
    public RegularTask(String title, String description, Status status, Duration duration, LocalDateTime startTime) {
        // Если в супер классе есть идентификатор по умолчанию, можно присвоить -1 или использовать иной механизм
        super(title, description, status, duration, startTime);  // Передаем -1 как временный идентификатор
    }

    @Override
    public TaskType getType() {
        return TaskType.Task;  // Вернём тип задачи
    }

    @Override
    public String toString() {
        return "RegularTask{" +
                "id=" + getId() +  // getId() должен быть методом в супер классе Task
                ", title='" + getTitle() + '\'' +  // getTitle() должен быть методом в супер классе Task
                ", description='" + getDescription() + '\'' +  // Аналогично для getDescription()
                ", status=" + getStatus() +  // Аналогично для getStatus()
                ", duration=" + getDuration().toMinutes() + " minutes" +  // Аналогично для getDuration()
                ", startTime=" + getStartTime() +  // Аналогично для getStartTime()
                '}';
    }
}