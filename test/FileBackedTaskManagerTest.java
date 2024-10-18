import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    private FileBackedTaskManager fileBackedTaskManager;
    private final File testFile = new File("test_tasks.csv");

    @BeforeEach
    public void setUp() {
        fileBackedTaskManager = new FileBackedTaskManager(testFile);
    }

    @Test
    public void testSaveAndLoadTasks() throws IOException {
        // Создание задачи и сохранение в файл
        Task task = new RegularTask("Task 1", "Description 1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2024, 10, 15, 10, 0));
        fileBackedTaskManager.createTask(task);
        fileBackedTaskManager.save();

        // Создаем новый менеджер и загружаем данные из файла
        FileBackedTaskManager newManager = new FileBackedTaskManager(testFile);
        newManager.load();

        Task loadedTask = newManager.getTaskById(task.getId());
        assertNotNull(loadedTask, "Task should be loaded from file");
        assertEquals(task.getTitle(), loadedTask.getTitle(), "Loaded task title should match");
        assertEquals(task.getStartTime(), loadedTask.getStartTime(), "Loaded task start time should match");
    }
}