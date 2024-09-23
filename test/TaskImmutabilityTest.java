import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskImmutabilityTest {

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task(1, "Task 1", "Description 1", Status.NEW);
    }

    @Test
    void testImmutability() {
        // Проверяем, что заголовок задачи не изменяется
        assertEquals("Task 1", task.getTitle(), "Title should not change.");

        // Проверяем, что описание задачи не изменяется
        assertEquals("Description 1", task.getDescription(), "Description should not change.");

        // Проверяем, что статус задачи не изменяется
        assertEquals(Status.NEW, task.getStatus(), "Status should not change.");
    }
}