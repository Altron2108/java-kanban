import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Map<Integer, Subtask> subtasks = new HashMap<>();
    protected Set<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(Task::getId)
    );

    protected HistoryManager historyManager = new InMemoryHistoryManager();

    private int nextId = 1;

    // Геттер для поля nextId
    public int getNextId() {
        return nextId;
    }

    @Override
    public void addTask(Task task) throws ManagerSaveException {
        validateTask(task);
        task.id = nextId++;
        tasks.put(task.id, task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void addEpic(Epic epic) throws ManagerSaveException {
        epic.id = nextId++;
        epics.put(epic.id, epic);
    }

    @Override
    public void addSubtask(Subtask subtask) throws ManagerSaveException {
        if (!epics.containsKey(subtask.getEpicId())) {
            throw new ManagerSaveException("Epic с id " + subtask.getEpicId() + " не существует.");
        }
        validateTask(subtask);
        subtask.id = nextId++;
        subtasks.put(subtask.id, subtask);
        epics.get(subtask.getEpicId()).addSubtask(subtask.id);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
        updateEpic(subtask.getEpicId());
    }

    @Override
    public void updateTask(Task task) throws ManagerSaveException {
        if (!tasks.containsKey(task.id)) {
            throw new ManagerSaveException("Task с id " + task.id + " не найден.");
        }
        validateTask(task);
        Task existingTask = tasks.get(task.id);
        if (existingTask.getStartTime() != null) {
            prioritizedTasks.remove(existingTask);
        }
        tasks.put(task.id, task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void updateEpic(Epic epic) throws ManagerSaveException {
        if (!epics.containsKey(epic.id)) {
            throw new ManagerSaveException("Epic с id " + epic.id + " не найден.");
        }
        epics.put(epic.id, epic);
        List<Subtask> epicSubtasks = getEpicSubtasks(epic.id);
        epic.updateFields(epicSubtasks);
    }

    @Override
    public void updateSubtask(Subtask subtask) throws ManagerSaveException {
        if (!subtasks.containsKey(subtask.id)) {
            throw new ManagerSaveException("Subtask с id " + subtask.id + " не найден.");
        }
        validateTask(subtask);
        Subtask existingSubtask = subtasks.get(subtask.id);
        if (existingSubtask.getStartTime() != null) {
            prioritizedTasks.remove(existingSubtask);
        }
        subtasks.put(subtask.id, subtask);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
        updateEpic(subtask.getEpicId());
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private void validateTask(Task task) throws ManagerSaveException {
        if (task.getStartTime() == null || task.getDuration() == null) {
            return; // Задачи без startTime не проверяются на пересечения
        }
        boolean overlap = prioritizedTasks.stream().anyMatch(t -> isOverlapping(t, task));
        if (overlap) {
            throw new ManagerSaveException("Задача пересекается с существующими задачами.");
        }
    }

    private boolean isOverlapping(Task t1, Task t2) {
        LocalDateTime start1 = t1.getStartTime();
        LocalDateTime end1 = t1.getEndTime();
        LocalDateTime start2 = t2.getStartTime();
        LocalDateTime end2 = t2.getEndTime();

        if (start1 == null || end1 == null || start2 == null || end2 == null) {
            return false;
        }

        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    private void updateEpic(int epicId) {
        Epic epic = epics.get(epicId);
        List<Subtask> epicSubtasks = getEpicSubtasks(epicId);
        epic.updateFields(epicSubtasks);
    }

    @Override
    public void save() throws IOException {
        // Реализация сохранения в файл (например, в формате CSV)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tasks.csv"))) {
            writer.write("id,type,name,status,description,duration,startTime,epic\n");
            for (Task task : tasks.values()) {
                writer.write(taskToString(task));
                writer.newLine();
            }
            for (Epic epic : epics.values()) {
                writer.write(taskToString(epic));
                writer.newLine();
            }
            for (Subtask subtask : subtasks.values()) {
                writer.write(taskToString(subtask));
                writer.newLine();
            }

        }
    }

    protected String taskToString(Task task) {
        String type;
        if (task instanceof Epic) {
            type = "EPIC";
        } else if (task instanceof Subtask) {
            type = "SUBTASK";
        } else {
            type = "TASK";
        }
        String epicId = "";
        if (task instanceof Subtask) {
            epicId = String.valueOf(((Subtask) task).getEpicId());
        }
        return String.join(",",
                String.valueOf(task.id),
                type,
                escapeComma(task.getName()),
                task.getStatus().name(),
                escapeComma(task.getDescription()),
                task.getDuration() != null ? String.valueOf(task.getDuration().toMinutes()) : "",
                task.getStartTime() != null ? task.getStartTime().toString() : "",
                epicId
        );
    }

    private String escapeComma(String value) {
        if (value.contains(",")) {
            return "\"" + value + "\"";
        }
        return value;
    }

    @Override
    public void load() throws IOException, ManagerSaveException {
        throw new UnsupportedOperationException("Метод load() не реализован.");
    }

    public List<Subtask> getEpicSubtasks(int epicId) {
        return subtasks.values().stream()
                .filter(subtask -> subtask.getEpicId() == epicId)
                .collect(Collectors.toList());
    }

    @Override
    public void removeTask(int id) {
        Task task = tasks.remove(id);
        if (task != null && task.getStartTime() != null) {
            prioritizedTasks.remove(task);
        }
        historyManager.remove(id);
    }

    @Override
    public void removeEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtaskIds()) {
                Subtask subtask = subtasks.remove(subtaskId);
                if (subtask != null && subtask.getStartTime() != null) {
                    prioritizedTasks.remove(subtask);
                }
                historyManager.remove(subtaskId);
            }
            historyManager.remove(id);
        }
    }

    @Override
    public void removeSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtask(id);
                updateEpic(epic.id);
            }
            if (subtask.getStartTime() != null) {
                prioritizedTasks.remove(subtask);
            }
            historyManager.remove(id);
        }
    }

    @Override
    public void createTask(Task task1) {

    }

    @Override
    public void createEpic(Epic epic) {

    }

    @Override
    public void createSubtask(Subtask subtask1) {

    }

    @Override
    public void deleteTaskById(int id) {

    }

    @Override
    public void deleteEpicById(int id) {

    }
}
