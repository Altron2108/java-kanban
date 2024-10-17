import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubtaskTest {

    @Test
    public void constructor_ShouldInitializeFieldsCorrectly() {
        Subtask subtask = new Subtask(
                1,                                // Идентификатор подзадачи
                "Subtask Title",                   // Название
                "Subtask Description",             // Описание
                Status.NEW,                        // Статус
                1,                                 // Идентификатор эпика
                Duration.ofHours(2),               // Продолжительность задачи
                LocalDateTime.now()                // Время начала задачи
        );

        assertEquals("Subtask Title", subtask.getName());            // Метод getName(), а не getTitle()
        assertEquals("Subtask Description", subtask.getDescription());
        assertEquals(Status.NEW, subtask.getStatus());
        assertEquals(1, subtask.getEpicId());
    }
}
