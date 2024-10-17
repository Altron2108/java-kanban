import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaskTest {


    @Test
    public void setTitle_setDescription_setStatus_ShouldUpdateTaskFields() {
        // Создаем задачу с корректным конструктором
        Task task = new Task(1, "Title", "Description", Status.NEW, Duration.ofHours(1),
                LocalDateTime.now());

        // Меняем значения полей через сеттеры
        task.setTitle("New Title");
        task.setDescription("New Description");
        task.setStatus(Status.IN_PROGRESS);

        // Проверяем, что значения изменились
        assertEquals("New Title", task.getTitle());
        assertEquals("New Description", task.getDescription());
        assertEquals(Status.IN_PROGRESS, task.getStatus());
    }
}
