import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    @BeforeEach
    public abstract void setUp();

    @Test
    public void createTask_ShouldAssignUniqueIds() {
        LocalDateTime fixedTime = LocalDateTime.of(2024, 10, 18, 10, 0);
        Task task1 = new RegularTask("Title1", "Description1", Status.NEW,
                Duration.ofMinutes(60), fixedTime);
        Task task2 = new RegularTask("Title2", "Description2", Status.IN_PROGRESS,
                Duration.ofMinutes(30), fixedTime.plusMinutes(70));

        int id1 = taskManager.createTask(task1);
        int id2 = taskManager.createTask(task2);

        assertNotEquals(id1, id2, "Task IDs should be unique");
    }

    @Test
    public void deleteTaskById_ShouldRemoveTask() {
        Task task = new RegularTask("Title", "Description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.now());
        int taskId = taskManager.createTask(task);

        assertNotNull(taskManager.getTaskById(taskId), "Task should exist before deletion");
        taskManager.deleteTaskById(taskId);
        assertNull(taskManager.getTaskById(taskId), "Task should be null after deletion");
    }

    @Test
    public void getTasks_ShouldReturnAllTasks() {
        Task task1 = new RegularTask("Title1", "Description1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.now());
        Task task2 = new RegularTask("Title2", "Description2", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(70));

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        List<Task> tasks = taskManager.getTasks();
        assertEquals(2, tasks.size(), "Should return two tasks");
        assertTrue(tasks.contains(task1), "Should contain task1");
        assertTrue(tasks.contains(task2), "Should contain task2");
    }

    @Test
    public void updateTask_ShouldChangeExistingTask() {
        Task task = new RegularTask("Title", "Description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.now());
        int taskId = taskManager.createTask(task);

        Task updatedTask = new RegularTask("Updated Title", "Updated Description", Status.IN_PROGRESS,
                Duration.ofMinutes(120), LocalDateTime.now());
        updatedTask.setId(taskId);
        taskManager.updateTask(updatedTask);

        Task retrievedTask = taskManager.getTaskById(taskId);
        assertEquals("Updated Title", retrievedTask.getTitle(), "Task title should be updated");
        assertEquals("Updated Description", retrievedTask.getDescription(),
                "Task description should be updated");
        assertEquals(Status.IN_PROGRESS, retrievedTask.getStatus(), "Task status should be updated");
    }

    @Test
    public void removeAllTasks_ShouldClearAllTasks() {
        Task task1 = new RegularTask("Title1", "Description1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.now());
        Task task2 = new RegularTask("Title2", "Description2", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(70));

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.removeAllTasks();
        assertTrue(taskManager.getTasks().isEmpty(), "All tasks should be removed");
    }
}




