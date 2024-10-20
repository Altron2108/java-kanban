import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;
    private Epic epic1;
    private Subtask subtask1;
    private Subtask subtask2;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();

        epic1 = new Epic("Epic 1", "Epic Description");
        epic1.setId(1); // Уникальный ID для epic1

        subtask1 = new Subtask("Subtask 1", "Description 1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.now(), epic1.getId());
        subtask1.setId(2); // Уникальный ID для subtask1

        subtask2 = new Subtask("Subtask 2", "Description 2", Status.IN_PROGRESS,
                Duration.ofMinutes(120), LocalDateTime.now().plusMinutes(60), epic1.getId());
        subtask2.setId(3); // Уникальный ID для subtask2
    }


    @Test
    void addTasksToHistory_HistoryPreservesCorrectOrder() {
        historyManager.add(epic1);
        historyManager.add(subtask1);
        historyManager.add(subtask2);

        List<Task> history = historyManager.getHistory();

        assertEquals(3, history.size(), "History should contain three tasks.");
        // Исправлено количество
        assertEquals(epic1, history.get(0), "First task should be epic1.");
        assertEquals(subtask1, history.get(1), "Second task should be subtask1.");
        assertEquals(subtask2, history.get(2), "Third task should be subtask2.");
    }

    @Test
    void addSameTaskToHistory_TaskMovesToEnd() {
        historyManager.add(epic1);
        historyManager.add(subtask1);
        historyManager.add(subtask2);
        historyManager.add(subtask1); // Добавляем subtask1 снова

        List<Task> history = historyManager.getHistory();

        assertEquals(3, history.size(), "History should still contain three tasks.");
        assertEquals(epic1, history.get(0), "First task should be epic1.");
        assertEquals(subtask2, history.get(1), "Second task should be subtask2.");
        assertEquals(subtask1, history.get(2), "Third task should be subtask1.");
    }

    @Test
    void addDuplicateTask_HistoryDoesNotContainDuplicates() {
        historyManager.add(epic1);
        historyManager.add(subtask1);
        historyManager.add(epic1); // epic1 должна переместиться в конец
        historyManager.add(epic1); // epic1 должна остаться в конце

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "History should contain two unique tasks.");
        assertEquals(subtask1, history.get(0), "First task should be subtask1.");
        assertEquals(epic1, history.get(1), "Second task should be epic1.");
    }

    @Test
    void getHistory_HistoryReturnsUnmodifiableList() {
        historyManager.add(epic1);
        historyManager.add(subtask1);

        List<Task> history = historyManager.getHistory();
        assertThrows(UnsupportedOperationException.class, () -> history.add(subtask2),
                "History should be unmodifiable.");
    }

    @Test
    void removeTaskFromHistory_TaskIsRemoved() {
        historyManager.add(epic1);
        historyManager.add(subtask1);
        historyManager.remove(subtask1.getId());

        List<Task> history = historyManager.getHistory();

        assertEquals(1, history.size(), "History should contain one task after removal.");
        assertEquals(epic1, history.getFirst(), "Remaining task should be epic1.");
    }

    @Test
    void removeNonExistentTask_HistoryRemainsUnchanged() {
        historyManager.add(epic1);
        historyManager.add(subtask1);

        // Попытка удалить подзадачу, которая не была добавлена
        historyManager.remove(subtask2.getId());

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "History should remain unchanged."); // Исправлено количество
        assertEquals(epic1, history.get(0), "First task should still be epic1.");
        assertEquals(subtask1, history.get(1), "Second task should still be subtask1.");
    }

    @Test
    void getHistory_WhenNoTasksAdded_ReturnsEmptyList() {
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "History should be empty when no tasks have been added.");
    }
}
