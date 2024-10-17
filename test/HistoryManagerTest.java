import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    private final HistoryManager historyManager = Managers.getDefaultHistoryManager();
    private final Task task = new Task(
            1,                            // Идентификатор задачи
            "Test Task",
            "Test Description",
            Status.NEW,
            Duration.ofHours(1),           // Продолжительность задачи
            LocalDateTime.now()            // Время начала задачи
    );

    @Test
    void addTaskToHistory_TaskAdded_HistoryContainsTask() {
        historyManager.add(task);
        var history = historyManager.getHistory();

        assertNotNull(history, "History should not be null.");
        assertEquals(1, history.size(), "History should contain one task.");
        assertEquals(task, history.getFirst(), "Task in history should be equal to added task.");
    }
}
