import java.util.List;

public interface TaskManager {
    //  Метод для получения истории задач
    List<Task> getHistory();

    int createTask(Task task);
    Task getTaskById(int id);
    void updateTask(Task task);
    void deleteTaskById(int id);
    void removeAllTasks();
    void removeAllEpics();
    void removeAllSubtasks();
    List<Task> getTasks();
    List<Epic> getEpics();
    List<Subtask> getSubtasks();
    int createEpic(Epic epic);
    Epic getEpicById(int id);
    void updateEpic(Epic epic);
    void deleteEpicById(int id);
    Subtask getSubtaskById(int id);
    void updateSubtask(Subtask subtask);
    void createSubtask(Subtask subtask);
    void deleteSubtaskById(int id);


}
