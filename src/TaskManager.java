import java.util.List;

public interface TaskManager {
    // Методы для управления задачами
    int createTask(Task task);             // Создание задачи

    Task getTaskById(int id);

    void updateTask(Task task);            // Обновление задачи

    void deleteTaskById(int id);

    void removeAllTasks();                 // Удаление всех задач

    List<Task> getTasks();                 // Получение всех задач

    // Методы для управления эпиками
    void createEpic(Epic epic);             // Создание эпика

    Epic getEpicById(int id);

    void updateEpic(Epic epic);            // Обновление эпика

    void deleteEpicById(int id);

    void removeAllEpics();                 // Удаление всех эпиков

    List<Epic> getEpics();                 // Получение всех эпиков

    // Методы для управления подзадачами
    void createSubtask(Subtask subtask);    // Создание подзадачи

    Subtask getSubtaskById(int id);

    void updateSubtask(Subtask subtask);   // Обновление подзадачи

    void deleteSubtaskById(int id);

    void removeAllSubtasks();              // Удаление всех подзадач

    List<Subtask> getSubtasks();           // Получение всех подзадач

    // Получение списка задач по приоритету
    List<Task> getPrioritizedTasks();
}
