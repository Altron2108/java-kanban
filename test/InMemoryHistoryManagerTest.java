import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    public void setUp() {
        historyManager = new InMemoryHistoryManager();
        task1 = new Task(1, "Task 1", "Description 1", Status.NEW, Duration.ofMinutes(60), LocalDateTime.of(2024, 10, 15, 9, 0));
        task2 = new Task(2, "Task 2", "Description 2", Status.IN_PROGRESS, Duration.ofMinutes(30), LocalDateTime.of(2024, 10, 15, 10, 0));
        task3 = new Task(3, "Task 3", "Description 3", Status.DONE, Duration.ofMinutes(15), LocalDateTime.of(2024, 10, 15, 11, 0));
    }

    @Test
    public void testEmptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty(), "История должна быть пустой.");
    }

    @Test
    public void testAddAndGetHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        assertEquals(2, historyManager.getHistory().size(), "История должна содержать 2 задачи.");
        assertEquals(task1, historyManager.getHistory().get(0), "Первая задача в истории должна быть task1.");
        assertEquals(task2, historyManager.getHistory().get(1), "Вторая задача в истории должна быть task2.");
    }

    @Test
    public void testAddDuplicate() {
        historyManager.add(task1);
        historyManager.add(task1);
        assertEquals(1, historyManager.getHistory().size(), "История не должна содержать дубликатов.");
    }

    @Test
    public void testRemoveFromHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task1.id);
        assertEquals(1, historyManager.getHistory().size(), "После удаления должно остаться 1 задача.");
        assertEquals(task2, historyManager.getHistory().getFirst(), "Оставшаяся задача должна быть task2.");
    }

    @Test
    public void testRemoveFromBeginning() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task1.id);
        assertEquals(1, historyManager.getHistory().size(), "После удаления первой задачи должно остаться 1 задача.");
        assertEquals(task2, historyManager.getHistory().getFirst(), "Оставшаяся задача должна быть task2.");
    }

    @Test
    public void testRemoveFromMiddle() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task2.id);
        assertEquals(2, historyManager.getHistory().size(), "После удаления средней задачи должно остаться 2 задачи.");
        assertEquals(task1, historyManager.getHistory().get(0), "Первая задача должна быть task1.");
        assertEquals(task3, historyManager.getHistory().get(1), "Вторая задача должна быть task3.");
    }

    @Test
    public void testRemoveFromEnd() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task2.id);
        assertEquals(1, historyManager.getHistory().size(), "После удаления последней задачи должно остаться 1 задача.");
        assertEquals(task1, historyManager.getHistory().getFirst(), "Оставшаяся задача должна быть task1.");
    }

}
