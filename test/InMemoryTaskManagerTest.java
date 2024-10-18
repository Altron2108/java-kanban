import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    @Override
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
        assertDoesNotThrow(() -> taskManager.createTask(task2),
                "Creating overlapping task should not throw exception.");
    }

    @Test
    public void testTaskWithOverlapThrowsException() {
        Task task1 = new RegularTask("Task 1", "Description 1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2024, 10, 15, 10, 0));
        Task task2 = new RegularTask("Task 2", "Description 2", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2024, 10, 15, 10, 30));

        taskManager.createTask(task1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> taskManager.createTask(task2));
        assertEquals("Task time overlap detected.", exception.getMessage(),
                "Exception message should indicate a time overlap.");
    }

    @Test
    public void testUpdateTaskWithOverlapThrowsException() {
        Task task1 = new RegularTask("Task 1", "Description 1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2024, 10, 15, 10, 0));
        Task task2 = new RegularTask("Task 2", "Description 2", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2024, 10, 15, 12, 0));

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        // Обновляем task2 с пересечением времени
        task2 = new RegularTask("Task 2", "Description 2", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2024, 10, 15, 10, 30));

        Task finalTask = task2;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> taskManager.updateTask(finalTask));
        assertEquals("Task time overlap detected.", exception.getMessage(),
                "Exception message should indicate a time overlap on update.");
    }
}
