import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaskManagerTest {

    @Test
    public void createTask_ShouldAssignUniqueIds() {
        TaskManager manager = new InMemoryTaskManager();

        // Фиксированное время для тестов
        LocalDateTime fixedTime = LocalDateTime.of(2024, 10, 18, 10, 0);

        // Задача 1: Длительность 60 минут, начинается в fixedTime
        Task task1 = new RegularTask("Title1", "Description1", Status.NEW,
                Duration.ofMinutes(60), fixedTime);

        // Задача 2: Длительность 30 минут, начинается через 70 минут после task1 (не пересекается)
        Task task2 = new RegularTask("Title2", "Description2", Status.IN_PROGRESS,
                Duration.ofMinutes(30), fixedTime.plusMinutes(70));

        // Присваиваем уникальные идентификаторы
        int id1 = manager.createTask(task1);
        int id2 = manager.createTask(task2);

        // Проверка, что идентификаторы уникальны
        assertNotEquals(id1, id2, "Task IDs should be unique");
    }
}
