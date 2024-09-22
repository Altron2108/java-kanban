import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    private final HistoryManager historyManager = Managers.getDefaultHistoryManager();
    private final Task task = new Task("Test Task", "Test Description", Status.NEW);

    @Test
    void testAddToHistory() {
        historyManager.add(task);
        var history = historyManager.getHistory();

        assertNotNull(history, "History should not be null.");
        assertEquals(1, history.size(), "History should contain one task.");
        assertEquals(task, history.getFirst(), "Task in history should be equal to added task.");
    }
}