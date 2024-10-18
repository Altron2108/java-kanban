import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskIds;
    private Duration duration;
    private LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description, Status.NEW, Duration.ZERO, null);
        this.subtaskIds = new ArrayList<>();
        this.duration = Duration.ZERO;
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

    // Добавление метода для очистки подзадач
    public void clearSubtasks() {
        subtaskIds.clear();
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    // Добавляем метод для установки endTime
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void updateEpicTime(List<Subtask> subtasks) {
        if (subtasks.isEmpty()) {
            this.setStartTime(null);
            this.setDuration(Duration.ZERO);
            this.setEndTime(null);
        } else {
            LocalDateTime earliestStartTime = null;
            LocalDateTime latestEndTime = null;
            Duration totalDuration = Duration.ZERO;

            for (Subtask subtask : subtasks) {
                if (earliestStartTime == null || subtask.getStartTime().isBefore(earliestStartTime)) {
                    earliestStartTime = subtask.getStartTime();
                }
                if (latestEndTime == null || subtask.getEndTime().isAfter(latestEndTime)) {
                    latestEndTime = subtask.getEndTime();
                }
                totalDuration = totalDuration.plus(subtask.getDuration());
            }

            this.setStartTime(earliestStartTime);
            this.setDuration(totalDuration);
            this.setEndTime(latestEndTime);
        }
    }

    @Override
    public TaskType getType() {
        return TaskType.Epic;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subtaskIds=" + subtaskIds +
                ", duration=" + duration.toMinutes() + " minutes" +
                ", startTime=" + getStartTime() +
                ", endTime=" + endTime +
                '}';
    }
}
