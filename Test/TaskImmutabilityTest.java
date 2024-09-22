import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TaskImmutabilityTest {
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        // Передача HistoryManager в конструктор InMemoryTaskManager
        HistoryManager historyManager = Managers.getDefaultHistoryManager();
        taskManager = new InMemoryTaskManager(historyManager);
    }

    @Test
    public void testImmutability() {
        // Создание задачи
        Task task = new Task("Task 1", "Description 1", Status.NEW);
        int taskId = taskManager.createTask(task);

        // Получение задачи из менеджера и проверка её неизменности
        Task retrievedTask = taskManager.getTaskById(taskId);
        assertEquals(task, retrievedTask);

        // Попытка изменить свойства задачи через полученный объект
        retrievedTask.setTitle("Modified Title");
        retrievedTask.setDescription("Modified Description");
        retrievedTask.setStatus(Status.DONE);

        // Повторное получение задачи из менеджера
        Task reRetrievedTask = taskManager.getTaskById(taskId);

        // Проверка, что задача в менеджере осталась неизменной
        assertEquals("Task 1", reRetrievedTask.getTitle());
        assertEquals("Description 1", reRetrievedTask.getDescription());
        assertEquals(Status.NEW, reRetrievedTask.getStatus());
    }
}
