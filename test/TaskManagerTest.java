import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class TaskManagerTest {

    @Test
    public void testTaskUniqueness() {
        TaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task("Title1", "Description1", Status.NEW);
        Task task2 = new Task("Title2", "Description2", Status.IN_PROGRESS);

        int id1 = manager.createTask(task1);
        int id2 = manager.createTask(task2);

        assertNotEquals(id1, id2, "Task IDs should be unique");
    }
}