import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    protected List<Integer> subtaskIds;
    protected Duration duration; // Расчетное поле
    protected LocalDateTime startTime; // Расчетное поле
    protected LocalDateTime endTime; // Расчетное поле

    public Epic(int id, String name, String description) {
        super(id, name, description, Status.NEW, Duration.ZERO, null);
        this.subtaskIds = new ArrayList<>();
    }

    // Методы добавления/удаления подзадач
    public void addSubtask(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void removeSubtask(int subtaskId) {
        subtaskIds.remove(Integer.valueOf(subtaskId));
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void updateFields(List<Subtask> subtasks) {
        if (subtasks.isEmpty()) {
            this.duration = Duration.ZERO; // Устанавливаем продолжительность в 0
            this.startTime = null; // Если нет подзадач, startTime тоже можно установить в null
        } else {
            this.duration = subtasks.stream()
                    .map(subtask -> subtask.duration)
                    .reduce(Duration.ZERO, Duration::plus);
            this.startTime = subtasks.stream()
                    .map(subtask -> subtask.startTime)
                    .filter(Objects::nonNull)
                    .min(LocalDateTime::compareTo)
                    .orElse(null);
        }

        Duration totalDuration = Duration.ZERO;
        LocalDateTime earliest = null;
        LocalDateTime latest = null;
        boolean allNew = true;
        boolean allDone = true;

        for (Subtask subtask : subtasks) {
            totalDuration = totalDuration.plus(subtask.getDuration());
            if (subtask.getStartTime() != null) {
                if (earliest == null || subtask.getStartTime().isBefore(earliest)) {
                    earliest = subtask.getStartTime();
                }
                LocalDateTime subEnd = subtask.getEndTime();
                if (subEnd != null && (latest == null || subEnd.isAfter(latest))) {
                    latest = subtask.getEndTime();
                }
            }
            if (subtask.getStatus() != Status.NEW) {
                allNew = false;
            }
            if (subtask.getStatus() != Status.DONE) {
                allDone = false;
            }
        }

        this.duration = totalDuration;
        this.startTime = earliest;
        this.endTime = latest;

        if (allNew) {
            this.status = Status.NEW;
        } else if (allDone) {
            this.status = Status.DONE;
        } else {
            this.status = Status.IN_PROGRESS;
        }
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", subtaskIds=" + subtaskIds +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (!super.equals(o)) return false;

        Epic epic = (Epic) o;

        return Objects.equals(subtaskIds, epic.subtaskIds) &&
                Objects.equals(duration, epic.duration) &&
                Objects.equals(startTime, epic.startTime) &&
                Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds, duration, startTime, endTime);
    }

    // Метод для получения ID эпика
    public int getId() {
        return id;
    }
}
