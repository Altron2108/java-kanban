import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task1 = new Task(1, "Task 1", "Description 1", Status.NEW);
        task2 = new Task(2, "Task 2", "Description 2", Status.IN_PROGRESS);
        task3 = new Task(3, "Task 3", "Description 3", Status.DONE);
    }

    @Test
    void addTasksToHistory_HistoryPreservesCorrectOrder() {
        // Добавляем задачи в историю
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        List<Task> history = historyManager.getHistory();

        // Проверяем, что порядок соответствует добавлению
        assertEquals(3, history.size(), "History should contain three tasks.");
        assertEquals(task1, history.get(0), "First task should be task1.");
        assertEquals(task2, history.get(1), "Second task should be task2.");
        assertEquals(task3, history.get(2), "Third task should be task3.");
    }

    @Test
    void addSameTaskToHistory_TaskMovesToEnd() {
        // Добавляем задачи в историю
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        // Повторно добавляем task2
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();

        // Проверяем, что task2 переместилась в конец
        assertEquals(3, history.size(), "History should still contain three tasks.");
        assertEquals(task1, history.get(0), "First task should be task1.");
        assertEquals(task3, history.get(1), "Second task should be task3.");
        assertEquals(task2, history.get(2), "Third task should be task2.");
    }

    @Test
    void addDuplicateTask_HistoryDoesNotContainDuplicates() {
        // Добавляем одну задачу несколько раз
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1); // task1 должна переместиться в конец
        historyManager.add(task1); // task1 должна остаться в конце

        List<Task> history = historyManager.getHistory();

        // Проверяем, что истории содержит только две уникальные задачи и task1 в конце
        assertEquals(2, history.size(), "History should contain two unique tasks.");
        assertEquals(task2, history.get(0), "First task should be task2.");
        assertEquals(task1, history.get(1), "Second task should be task1.");
    }

    @Test
    void getHistory_HistoryReturnsUnmodifiableList() {
        // Добавляем задачи в историю
        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();


    }
}