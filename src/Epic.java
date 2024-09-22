import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskIds = new ArrayList<>();

    // Конструктор без ID
    public Epic(String title, String description) {
        super(title, description, Status.NEW);
    }

    public void addSubtask(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public List<Integer> getSubtaskIds() {
        return new ArrayList<>(subtaskIds);
    }

    @Override
    public String toString() {
        return "Epic{id=" + getId() + ", title='" + getTitle() + "', description='" + getDescription() + "', " +
                "status=" + getStatus() + ", subtaskIds=" + subtaskIds + '}';
    }
}
