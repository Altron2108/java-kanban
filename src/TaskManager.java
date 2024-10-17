import java.io.IOException;
import java.util.List;

public interface TaskManager {
    void addTask(Task task) throws ManagerSaveException;

    void addEpic(Epic epic) throws ManagerSaveException;

    void addSubtask(Subtask subtask) throws ManagerSaveException;

    void updateTask(Task task) throws ManagerSaveException;

    void updateEpic(Epic epic) throws ManagerSaveException;

    void updateSubtask(Subtask subtask) throws ManagerSaveException;

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    List<Task> getPrioritizedTasks();

    void save() throws IOException;

    void load() throws IOException, ManagerSaveException;

    void removeTask(int id);

    void removeEpic(int id);

    void removeSubtask(int id);

    // Дополнительные методы
    void createTask(Task task1) throws ManagerSaveException;

    void createEpic(Epic epic) throws ManagerSaveException;

    void createSubtask(Subtask subtask1) throws ManagerSaveException;

    void deleteTaskById(int id);

    void deleteEpicById(int id);
}
