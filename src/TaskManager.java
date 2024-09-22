import java.util.List;

public interface TaskManager {
    // Методы для управления задачами
    int createTask(Task task);

    Task getTaskById(int id);

    void updateTask(Task task);

    void deleteTaskById(int id);

    void removeAllTasks();

    List<Task> getTasks();

    // Методы для управления эпиками
    int createEpic(Epic epic);

    Epic getEpicById(int id);

    void updateEpic(Epic epic);

    void deleteEpicById(int id);

    void removeAllEpics();

    List<Epic> getEpics();

    // Методы для управления подзадачами
    int createSubtask(Subtask subtask);

    Subtask getSubtaskById(int id);

    void updateSubtask(Subtask subtask);

    void deleteSubtaskById(int id);

    void removeAllSubtasks();

    List<Subtask> getSubtasks();
}