import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SubtaskTest {
    @Test
    public void constructor_ShouldInitializeFieldsCorrectly() {
        Subtask subtask = new
                Subtask("Subtask Title", "Subtask Description", Status.NEW, 1);
        assertEquals("Subtask Title", subtask.getTitle());
        assertEquals("Subtask Description", subtask.getDescription());
        assertEquals(Status.NEW, subtask.getStatus());
        assertEquals(1, subtask.getEpicId());
    }
}