import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HistoryManager historyManager;
    private final List<Task> tasks = new ArrayList<>();
    private final List<Epic> epics = new ArrayList<>();
    private final List<Subtask> subtasks = new ArrayList<>();

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public int createTask(Task task) {
        tasks.add(task);
        return task.getId();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = findTaskById(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public void updateTask(Task task) {
        int index = findTaskIndexById(task.getId());
        if (index != -1) {
            tasks.set(index, task);
        }
    }

    @Override
    public void deleteTaskById(int id) {
        Task task = findTaskById(id);
        if (task != null) {
            tasks.remove(task);
        }
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        epics.clear();
    }

    @Override
    public void removeAllSubtasks() {
        subtasks.clear();
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics);
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks);
    }

    @Override
    public int createEpic(Epic epic) {
        epics.add(epic);
        return epic.getId();
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = findEpicById(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        int index = findEpicIndexById(epic.getId());
        if (index != -1) {
            epics.set(index, epic);
        }
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = findEpicById(id);
        if (epic != null) {
            epics.remove(epic);
        }
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = findSubtaskById(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int index = findSubtaskIndexById(subtask.getId());
        if (index != -1) {
            subtasks.set(index, subtask);
        }
    }

    @Override
    public void createSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = findSubtaskById(id);
        if (subtask != null) {
            subtasks.remove(subtask);
        }
    }

    // Реализация метода getHistory
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private Task findTaskById(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    private int findTaskIndexById(int id) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    private Epic findEpicById(int id) {
        for (Epic epic : epics) {
            if (epic.getId() == id) {
                return epic;
            }
        }
        return null;
    }

    private int findEpicIndexById(int id) {
        for (int i = 0; i < epics.size(); i++) {
            if (epics.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    private Subtask findSubtaskById(int id) {
        for (Subtask subtask : subtasks) {
            if (subtask.getId() == id) {
                return subtask;
            }
        }
        return null;
    }

    private int findSubtaskIndexById(int id) {
        for (int i = 0; i < subtasks.size(); i++) {
            if (subtasks.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }
}
