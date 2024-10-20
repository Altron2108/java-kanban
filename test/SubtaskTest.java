import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubtaskTest {
    @Test
    public void constructor_ShouldInitializeFieldsCorrectly() {
        Subtask subtask = new Subtask(
                "Subtask Title",
                "Subtask Description",
                Status.NEW,
                Duration.ofMinutes(30),  // добавлено поле duration
                LocalDateTime.now(),     // добавлено поле startTime
                1
        );
        assertEquals("Subtask Title", subtask.getTitle());
        assertEquals("Subtask Description", subtask.getDescription());
        assertEquals(Status.NEW, subtask.getStatus());
        assertEquals(1, subtask.getEpicId());
    }
}
