import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class EpicTest {
    @Test
    public void testEpicConstructor() {
        Epic epic = new Epic("Epic Title", "Epic Description");
        assertEquals("Epic Title", epic.getTitle());
        assertEquals("Epic Description", epic.getDescription());
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void testAddSubtask() {
        Epic epic = new Epic("Epic Title", "Epic Description");
        int subtaskId = 1;
        epic.addSubtask(subtaskId);
        assertTrue(epic.getSubtaskIds().contains(subtaskId));
    }
}