import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private final File testFile = new File("test_tasks.csv");

    @BeforeEach
    @Override
    public void setUp() {
        taskManager = new FileBackedTaskManager(testFile);
    }

    @Test
    public void testSaveAndLoadTasks() throws IOException {
        // Создание задачи и сохранение в файл
        Task task = new RegularTask("Task 1", "Description 1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2024, 10, 15, 16, 0));
        taskManager.createTask(task);
        taskManager.save();

        // Создаем новый менеджер и загружаем данные из файла
        FileBackedTaskManager newManager = new FileBackedTaskManager(testFile);
        newManager.load();

        Task loadedTask = newManager.getTaskById(task.getId());
        assertNotNull(loadedTask, "Task should be loaded from file");
        assertEquals(task.getTitle(), loadedTask.getTitle(), "Loaded task title should match");
        assertEquals(task.getStartTime(), loadedTask.getStartTime(), "Loaded task start time should match");
    }
    @Test
    public void getPrioritizedTasks_ShouldReturnTasksInOrder() {
        // Задачи не пересекаются, так как у task1 начало через 3 часа после task2
        Task task1 = new RegularTask("Task 1", "Description 1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2024, 10, 15, 13, 0));
        // Начало в 13:00
        Task task2 = new RegularTask("Task 2", "Description 2", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2024, 10, 15, 10, 0));
        // Начало в 10:00

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        assertEquals(2, prioritizedTasks.size(), "Should return two tasks in prioritized order.");
        assertEquals(task2, prioritizedTasks.get(0), "First task should be task2.");
        assertEquals(task1, prioritizedTasks.get(1), "Second task should be task1.");
    }
}
