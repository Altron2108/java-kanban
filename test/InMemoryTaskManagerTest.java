import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    private InMemoryTaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void testTaskWithoutOverlap() {
        Task task1 = new RegularTask("Task 1", "Description 1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2024, 10, 15, 10, 0));
        Task task2 = new RegularTask("Task 2", "Description 2", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2024, 10, 15, 12, 0));

        taskManager.createTask(task1);
        assertDoesNotThrow(() -> taskManager.createTask(task2));
    }

    @Test
    public void testTaskWithOverlapThrowsException() {
        Task task1 = new RegularTask("Task 1", "Description 1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2024, 10, 15, 10, 0));
        Task task2 = new RegularTask("Task 2", "Description 2", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2024, 10, 15, 10, 30));
        // Перекрытие по времени

        taskManager.createTask(task1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> taskManager.createTask(task2));
        assertEquals("Task time overlap detected.", exception.getMessage());
    }


    @Test
    public void testUpdateTaskWithOverlapThrowsException() {
        Task task1 = new RegularTask("Task 1", "Description 1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2024, 10, 15, 10, 0));
        Task task2 = new RegularTask("Task 2", "Description 2", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2024, 10, 15, 12, 0));

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        // Обновление task2 с пересечением времени
        task2 = new RegularTask("Task 2", "Description 2", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2024, 10, 15, 10, 30));

        Task finalTask = task2;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> taskManager.updateTask(finalTask));
        assertEquals("Task time overlap detected.", exception.getMessage());
    }

    @Test
    public void getPrioritizedTasks_ShouldReturnTasksInOrder() {
        TaskManager manager = new InMemoryTaskManager();

        Task task1 = new RegularTask("Task 1", "Description 1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.now());
        Task task2 = new RegularTask("Task 2", "Description 2", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.now().plusHours(1));

        manager.createTask(task1);
        manager.createTask(task2);

        List<Task> prioritizedTasks = manager.getPrioritizedTasks();

        assertEquals(task1, prioritizedTasks.get(0));
        assertEquals(task2, prioritizedTasks.get(1));
    }
}


