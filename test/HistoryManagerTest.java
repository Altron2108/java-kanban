import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    private final HistoryManager historyManager = Managers.getDefaultHistoryManager();
    // Используем конкретный подкласс RegularTask
    private final Task task = new RegularTask("Test Task", "Test Description",
            Status.NEW, Duration.ofMinutes(60), LocalDateTime.now());

    @Test
    void addTaskToHistory_TaskAdded_HistoryContainsTask() {
        historyManager.add(task);
        var history = historyManager.getHistory();

        assertNotNull(history, "History should not be null.");
        assertEquals(1, history.size(), "History should contain one task.");
        assertEquals(task, history.get(0), "Task in history should be equal to added task.");
    }
}