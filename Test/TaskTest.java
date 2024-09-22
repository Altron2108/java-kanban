import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TaskTest {
    @Test
    public void testTaskConstructor() {
        Task task = new Task("Title", "Description", Status.NEW);
        assertEquals("Title", task.getTitle());
        assertEquals("Description", task.getDescription());
        assertEquals(Status.NEW, task.getStatus());
    }

    @Test
    public void testTaskSetters() {
        Task task = new Task("Title", "Description", Status.NEW);
        task.setTitle("New Title");
        task.setDescription("New Description");
        task.setStatus(Status.IN_PROGRESS);
        assertEquals("New Title", task.getTitle());
        assertEquals("New Description", task.getDescription());
        assertEquals(Status.IN_PROGRESS, task.getStatus());
    }
}