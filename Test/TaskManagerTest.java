import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefaultTaskManager();
    }

    @Test
    void testAddTask() {
        // Использование класса Task напрямую
        Task task = new Task("Test Task", "Test Description", Status.NEW);
        int taskId = taskManager.createTask(task);

        Task retrievedTask = taskManager.getTaskById(taskId);
        assertNotNull(retrievedTask, "Task should be found.");
        assertEquals(task, retrievedTask, "Tasks should be equal.");
    }

    @Test
    void testTaskUniqueness() {
        // Создание двух разных задач
        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW);

        int id1 = taskManager.createTask(task1);
        int id2 = taskManager.createTask(task2);

        assertNotEquals(id1, id2, "Task IDs should be unique.");
    }
}
