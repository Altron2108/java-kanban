import java.util.List;

public interface TaskManager {
    void addTask(Task task);

    // Методы для управления задачами
    int createTask(Task task);             // Создание задачи

    Task getTaskById(int id);              // Получение задачи по ID

    void updateTask(Task task);            // Обновление задачи

    void deleteTaskById(int id);           // Удаление задачи по ID

    void removeAllTasks();                 // Удаление всех задач

    List<Task> getTasks();                 // Получение всех задач

    // Методы для управления эпиками
    void createEpic(Epic epic);            // Создание эпика

    Epic getEpicById(int id);              // Получение эпика по ID

    void updateEpic(Epic epic);            // Обновление эпика

    void deleteEpicById(int id);           // Удаление эпика по ID

    void removeAllEpics();                 // Удаление всех эпиков

    List<Epic> getEpics();                 // Получение всех эпиков

    // Методы для управления подзадачами
    void createSubtask(Subtask subtask);    // Создание подзадачи

    Subtask getSubtaskById(int id);        // Получение подзадачи по ID

    void updateSubtask(Subtask subtask);   // Обновление подзадачи

    void deleteSubtaskById(int id);        // Удаление подзадачи по ID

    void removeAllSubtasks();               // Удаление всех подзадач

    List<Subtask> getSubtasks();           // Получение всех подзадач

    // Получение списка задач по приоритету
    List<Task> getPrioritizedTasks();       // Получение задач по приоритету

    // Получение истории задач
    List<Task> getHistory();                // Получение истории просмотренных задач
}
