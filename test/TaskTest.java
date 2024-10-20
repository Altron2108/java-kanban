import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class TaskTest {

    @Test
    public void taskConstructor_ShouldInitializeFieldsCorrectly() {
        RegularTask task = new RegularTask("Title", "Description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.now());
        assertEquals("Title", task.getTitle());
        assertEquals("Description", task.getDescription());
        assertEquals(Status.NEW, task.getStatus());
    }

    @Test
    public void setTitle_setDescription_setStatus_ShouldUpdateTaskFields() {
        RegularTask task = new RegularTask("Title", "Description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.now());
        task.setTitle("New Title");
        task.setDescription("New Description");
        task.setStatus(Status.IN_PROGRESS);
        assertEquals("New Title", task.getTitle());
        assertEquals("New Description", task.getDescription());
        assertEquals(Status.IN_PROGRESS, task.getStatus());
    }
}
