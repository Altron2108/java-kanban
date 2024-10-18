import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskImmutabilityTest {

    private RegularTask task; // Используйте RegularTask вместо абстрактного Task

    @BeforeEach
    void setUp() {
        task = new RegularTask("Task 1", "Description 1", Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.now());
    }

    @Test
    void getTitle_ShouldReturnInitialTitle() {
        assertEquals("Task 1", task.getTitle(), "Title should be 'Task 1' after creation.");
    }

    @Test
    void getDescription_ShouldReturnInitialDescription() {
        assertEquals("Description 1", task.getDescription(), "Description should be 'Description 1' " +
                "after creation.");
    }

    @Test
    void getStatus_ShouldReturnInitialStatus() {
        assertEquals(Status.NEW, task.getStatus(), "Status should be 'NEW' after creation.");
    }
}
