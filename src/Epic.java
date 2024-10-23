import com.google.gson.annotations.Expose;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    @Expose
    private final List<Integer> subtaskIds; // Список идентификаторов подзадач
    @Expose
    private LocalDateTime endTime;           // Время завершения эпика

    public Epic(String title, String description) {
        super(title, description, Status.NEW, Duration.ZERO, null); // Вызываем конструктор суперкласса
        this.subtaskIds = new ArrayList<>();
        this.endTime = null; // Инициализируем время завершения
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtask(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void removeSubtask(int subtaskId) {
        subtaskIds.remove(Integer.valueOf(subtaskId));
    }

    public void clearSubtasks() {
        subtaskIds.clear(); // Очистка списка подзадач
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime; // Установка времени завершения
    }

    @Override
    public void setDuration(Duration duration) {
        super.setDuration(duration); // Вызываем метод родительского класса
    }

    public void updateEpicTime(List<Subtask> subtasks) {
        if (subtasks.isEmpty()) {
            this.setStartTime(null);
            this.setEndTime(null);
        } else {
            LocalDateTime earliestStartTime = null;
            LocalDateTime latestEndTime = null;

            for (Subtask subtask : subtasks) {
                if (earliestStartTime == null || subtask.getStartTime().isBefore(earliestStartTime)) {
                    earliestStartTime = subtask.getStartTime();
                }
                if (latestEndTime == null || subtask.getEndTime().isAfter(latestEndTime)) {
                    latestEndTime = subtask.getEndTime();
                }
            }

            this.setStartTime(earliestStartTime);
            this.setEndTime(latestEndTime);
        }
    }

    @Override
    public TaskType getType() {
        return TaskType.Epic; // Возвращаем тип задачи
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subtaskIds=" + subtaskIds +
                ", duration=" + getDuration().toMinutes() + " minutes" + // Используем метод getDuration() из Task
                ", startTime=" + getStartTime() +
                ", endTime=" + endTime +
                '}';
    }
}
