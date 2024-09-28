import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HistoryManager historyManager;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private int taskId = 1;

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistoryManager();
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    private int genId() {
        return taskId++;
    }

    @Override
    public int createTask(Task task) {
        task.setId(genId());
        tasks.put(task.getId(), task);
        historyManager.add(task);
        return task.getId();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
            historyManager.add(task);
        }
    }

    @Override
    public void deleteTaskById(int id) {
        Task removedTask = tasks.remove(id);
        if (removedTask != null) {
            historyManager.remove(id);
        }
    }

    @Override
    public void removeAllTasks() {
        for (int id : new ArrayList<>(tasks.keySet())) {
            deleteTaskById(id);
        }
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public int createEpic(Epic epic) {
        int id = genId();
        epic.setId(id);
        epics.put(id, epic);
        historyManager.add(epic);
        return id;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            historyManager.add(epic);
            refreshEpicStatus(epic.getId());
        }
    }

    @Override
    public void deleteEpicById(int id) {
        Epic removedEpic = epics.remove(id);
        if (removedEpic != null) {
            historyManager.remove(id);
            // Также удаляем все подзадачи, связанные с этим эпиком
            for (Integer subtaskId : new ArrayList<>(removedEpic.getSubtaskIds())) {
                deleteSubtaskById(subtaskId);
            }
        }
    }

    @Override
    public void removeAllEpics() {
        for (int id : new ArrayList<>(epics.keySet())) {
            deleteEpicById(id);
        }
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public int createSubtask(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            throw new IllegalArgumentException("Epic with id " + subtask.getEpicId() + " not found.");
        }
        int id = genId();
        subtask.setId(id);
        subtasks.put(id, subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(id);
        historyManager.add(subtask);
        refreshEpicStatus(epic.getId());
        return id;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            historyManager.add(subtask);
            refreshEpicStatus(subtask.getEpicId());
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask removedSubtask = subtasks.remove(id);
        if (removedSubtask != null) {
            historyManager.remove(id);
            Epic epic = epics.get(removedSubtask.getEpicId());
            if (epic != null) {
                epic.removeSubtask(id);
                refreshEpicStatus(epic.getId());
            }
        }
    }

    @Override
    public void removeAllSubtasks() {
        for (int id : new ArrayList<>(subtasks.keySet())) {
            deleteSubtaskById(id);
        }
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    /**
     * Обновляет статус эпика на основе статусов его подзадач.
     *
     * @param epicId Идентификатор эпика, статус которого необходимо обновить.
     */
    private void refreshEpicStatus(Integer epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }

        List<Integer> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        int countNew = 0;
        int countDone = 0;

        for (Integer subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask != null) {
                if (subtask.getStatus() == Status.NEW) {
                    countNew++;
                } else if (subtask.getStatus() == Status.DONE) {
                    countDone++;
                }
            }
        }

        if (countNew == subtaskIds.size()) {
            epic.setStatus(Status.NEW);
        } else if (countDone == subtaskIds.size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}