import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private int currentId = 0;

    public void createTask(Task task) {
        task.setId(++currentId);
        tasks.put(task.getId(), task);
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void createEpic(Epic epic) {
        epic.setId(++currentId);
        epics.put(epic.getId(), epic);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
    }

    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteEpicById(int id) {
        epics.remove(id);
        subtasks.values().removeIf(subtask -> subtask.getEpicId() == id);
    }

    public void createSubtask(Subtask subtask) {
        subtask.setId(++currentId);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = getEpicById(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtaskId(subtask.getId());
            updateEpicStatus(epic.getId());
        }
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public void removeAllSubtasks() {
        subtasks.clear();
        epics.values().forEach(epic -> epic.getSubtaskIds().clear());
    }

    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = getEpicById(subtask.getEpicId());
            if (epic != null) {
                epic.getSubtaskIds().remove(Integer.valueOf(id));
                updateEpicStatus(epic.getId());
            }
        }
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            boolean allNew = true;
            boolean allDone = true;

            for (int subtaskId : epic.getSubtaskIds()) {
                Subtask subtask = subtasks.get(subtaskId);
                if (subtask != null) {
                    if (subtask.getStatus() != Status.NEW) {
                        allNew = false;
                    }
                    if (subtask.getStatus() != Status.DONE) {
                        allDone = false;
                    }
                }
            }

            if (allNew) {
                epic.setStatus(Status.NEW);
            } else if (allDone) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }
}
