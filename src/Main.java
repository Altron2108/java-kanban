import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        // Создание задачи с автоматическим присвоением ID
        Task task1 = new Task(0, "Task 1", "Description 1", Status.NEW, Duration.ofMinutes(60), LocalDateTime.of(2024, 10, 15, 9, 0));
        taskManager.createTask(task1);

        // Создание эпика
        Epic epic1 = new Epic(0, "Epic 1", "Epic Description");
        taskManager.createEpic(epic1);

        // Создание подзадачи с автоматическим присвоением ID и связыванием с эпиком
        Subtask subtask1 = new Subtask(0, "Subtask 1", "Sub Description 1", Status.NEW, epic1.id, Duration.ofMinutes(30), LocalDateTime.of(2024, 10, 15, 10, 0));
        taskManager.createSubtask(subtask1);

        // Получение и вывод приоритетных задач
        List<Task> prioritized = taskManager.getPrioritizedTasks();
        for (Task task : prioritized) {
            System.out.println(task);
        }

        // Сохранение задач в файл
        try {
            taskManager.save();
        } catch (IOException e) {
            e.fillInStackTrace();
        }

        // Загрузка задач из файла
        FileBackedTaskManager loadedManager = new FileBackedTaskManager("tasks.csv");
        try {
            loadedManager.load();
        } catch (IOException | ManagerSaveException e) {
            e.fillInStackTrace();
        }

        // Вывод загруженных задач
        List<Task> loadedTasks = loadedManager.getAllTasks();
        for (Task task : loadedTasks) {
            System.out.println(task);
        }

        List<Epic> loadedEpics = loadedManager.getAllEpics();
        for (Epic epic : loadedEpics) {
            System.out.println(epic);
        }

        List<Subtask> loadedSubtasks = loadedManager.getAllSubtasks();
        for (Subtask subtask : loadedSubtasks) {
            System.out.println(subtask);
        }

        // Вывод истории
        List<Task> history = loadedManager.historyManager.getHistory();
        System.out.println("История задач:");
        for (Task task : history) {
            System.out.println(task);
        }
    }
}
