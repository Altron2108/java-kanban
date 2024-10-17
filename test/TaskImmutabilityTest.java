import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskImmutabilityTest {

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task(
                1,                                   // Идентификатор задачи
                "Task 1",                            // Название задачи
                "Description 1",                     // Описание задачи
                Status.NEW,                          // Статус задачи
                Duration.ofHours(1),                 // Продолжительность задачи
                LocalDateTime.now()                  // Время начала задачи
        );
    }

    @Test
    void getName_ShouldReturnInitialName() {
        assertEquals("Task 1", task.getName(), "Name should be 'Task 1' after creation.");
    }

    @Test
    void getDescription_ShouldReturnInitialDescription() {
        System.out.println("Current description: " + task.getDescription());
        assertEquals("Description 1", task.getDescription(), "Description should be " +
                "'Description 1' after creation.");
    }

    @Test
    void getStatus_ShouldReturnInitialStatus() {
        System.out.println("Current status: " + task.getStatus());
        assertEquals(Status.NEW, task.getStatus(), "Status should be 'NEW' after creation.");
    }
}

