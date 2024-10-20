import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    private int idCounter = 1;  // Счетчик для уникальных идентификаторов

    private int generateUniqueId() {
        return idCounter++;
    }

    @Override
    public int createTask(Task task) {
        task.setId(generateUniqueId());  // Присваиваем уникальный ID
        validateTaskOverlap(task);       // Валидация пересечений перед созданием задачи
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);      // Добавляем задачу в отсортированное множество
        return task.getId();
    }

    @Override
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    @Override
    public void updateTask(Task task) {
        Task oldTask = tasks.get(task.getId());
        if (oldTask != null) {
            prioritizedTasks.remove(oldTask);
        }
        validateTaskOverlap(task);       // Валидация пересечений перед обновлением задачи
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);      // Обновляем задачу в отсортированном множестве
    }

    @Override
    public void deleteTaskById(int id) {
        Task task = tasks.remove(id);
        if (task != null) {
            prioritizedTasks.remove(task);  // Удаляем задачу из отсортированного множества
        }
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
        prioritizedTasks.clear();
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(generateUniqueId());  // Присваиваем уникальный ID
        epics.put(epic.getId(), epic);
    }

    @Override
    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        updateEpicTime(epic);
        updateEpicStatus(epic);  // Обновляем статус эпика при изменении
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (int subtaskId : epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
                prioritizedTasks.remove(subtasks.get(subtaskId));  // Удаляем подзадачу из приоритетного списка
            }
        }
    }

    @Override
    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
        prioritizedTasks.clear();
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void createSubtask(Subtask subtask) {
        subtask.setId(generateUniqueId());  // Присваиваем уникальный ID
        validateTaskOverlap(subtask);       // Валидация пересечений перед созданием подзадачи
        subtasks.put(subtask.getId(), subtask);
        Epic epic = getEpicById(subtask.getEpicId());
        epic.addSubtask(subtask.getId());
        updateEpicTime(epic);
        prioritizedTasks.add(subtask);      // Добавляем подзадачу в отсортированное множество
        updateEpicStatus(epic);             // Обновляем статус эпика при добавлении подзадачи
    }

    @Override
    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask oldSubtask = subtasks.get(subtask.getId());
        if (oldSubtask != null) {
            prioritizedTasks.remove(oldSubtask);
        }
        validateTaskOverlap(subtask);       // Валидация пересечений перед обновлением подзадачи
        subtasks.put(subtask.getId(), subtask);
        Epic epic = getEpicById(subtask.getEpicId());
        updateEpicTime(epic);
        prioritizedTasks.add(subtask);      // Обновляем подзадачу в отсортированном множестве
        updateEpicStatus(epic);             // Обновляем статус эпика при изменении подзадачи
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = getEpicById(subtask.getEpicId());
            epic.removeSubtask(id);
            updateEpicTime(epic);
            prioritizedTasks.remove(subtask);  // Удаляем подзадачу из множества
            updateEpicStatus(epic);            // Обновляем статус эпика при удалении подзадачи
        }
    }

    @Override
    public void removeAllSubtasks() {
        subtasks.clear();
        prioritizedTasks.clear();  // Очищаем задачи, связанные с подзадачами
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // Метод для проверки пересечений задач
    private void validateTaskOverlap(Task newTask) {
        boolean hasOverlap = prioritizedTasks.stream()
                .filter(task -> task.getId() != newTask.getId()) // Исключаем саму задачу
                .anyMatch(task -> isTimeOverlapping(newTask, task));

        if (hasOverlap) {
            throw new IllegalArgumentException("Task time overlap detected.");
        }
    }

    // Проверка пересечения двух задач по времени
    private boolean isTimeOverlapping(Task task1, Task task2) {
        return task1.getStartTime() != null && task2.getStartTime() != null &&
                task1.getEndTime() != null && task2.getEndTime() != null &&
                (task1.getStartTime().isBefore(task2.getEndTime()) && task1.getEndTime().isAfter(task2.getStartTime()));
    }

    // Метод для обновления времени эпика на основе подзадач
    public void updateEpicTime(Epic epic) {
        List<Subtask> epicSubtasks = epic.getSubtaskIds().stream()
                .map(this::getSubtaskById)
                .filter(Objects::nonNull)
                .toList();

        if (epicSubtasks.isEmpty()) {
            epic.setStartTime(null);
            epic.setDuration(Duration.ZERO);
            epic.setEndTime(null);
        } else {
            LocalDateTime earliestStartTime = epicSubtasks.stream()
                    .map(Task::getStartTime)
                    .min(LocalDateTime::compareTo)
                    .orElse(null);

            LocalDateTime latestEndTime = epicSubtasks.stream()
                    .map(Task::getEndTime)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);

            Duration totalDuration = epicSubtasks.stream()
                    .map(Task::getDuration)
                    .reduce(Duration.ZERO, Duration::plus);

            epic.setStartTime(earliestStartTime);
            epic.setDuration(totalDuration);
            epic.setEndTime(latestEndTime);
        }
    }

    // Метод для обновления статуса эпика на основе подзадач
    public void updateEpicStatus(Epic epic) {
        List<Subtask> subtasks = epic.getSubtaskIds().stream()
                .map(this::getSubtaskById)
                .filter(Objects::nonNull)
                .toList();

        if (subtasks.isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            boolean allDone = subtasks.stream().allMatch(subtask -> subtask.getStatus() == Status.DONE);
            boolean anyInProgress = subtasks.stream().anyMatch(subtask -> subtask.getStatus() == Status.IN_PROGRESS);

            if (allDone) {
                epic.setStatus(Status.DONE);
            } else if (anyInProgress) {
                epic.setStatus(Status.IN_PROGRESS);
            } else {
                epic.setStatus(Status.NEW);
            }
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        // Возвращаем задачи и подзадачи в отсортированном порядке
        return new ArrayList<>(prioritizedTasks);
    }

}
