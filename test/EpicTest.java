import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

public class EpicTest {

    @Test
    public void epicConstructor_ValidInput_EpicCreatedSuccessfully() {
        // Предполагаем, что id будет присвоен менеджером, поэтому передаём 0 или любое другое начальное значение
        Epic epic = new Epic(0, "Epic Title", "Epic Description");

        // Проверяем, что значения полей установлены корректно
        assertEquals("Epic Title", epic.getName(), "Название эпика должно совпадать с заданным.");
        assertEquals("Epic Description", epic.getDescription(), "Описание эпика должно совпадать с заданным.");
        assertEquals(Status.NEW, epic.getStatus(), "Статус эпика должен быть NEW по умолчанию.");

        // Дополнительно проверяем, что список подзадач пуст
        assertTrue(epic.getSubtaskIds().isEmpty(), "Список подзадач эпика должен быть пустым при создании.");
    }

    @Test
    public void testAddSubtask() {
        Epic epic = new Epic(1, "Epic Title", "Epic Description");
        int subtaskId = 101;

        // Добавляем подзадачу
        epic.addSubtask(subtaskId);

        // Проверяем, что подзадача добавлена в список
        assertTrue(epic.getSubtaskIds().contains(subtaskId), "Подзадача должна быть добавлена в список подзадач эпика.");
    }

    @Test
    public void testRemoveSubtask() {
        Epic epic = new Epic(2, "Epic Title", "Epic Description");
        int subtaskId = 202;

        // Добавляем подзадачу
        epic.addSubtask(subtaskId);
        assertTrue(epic.getSubtaskIds().contains(subtaskId), "Подзадача должна быть добавлена в список подзадач эпика.");

        // Удаляем подзадачу
        epic.removeSubtask(subtaskId);
        assertFalse(epic.getSubtaskIds().contains(subtaskId), "Подзадача должна быть удалена из списка подзадач эпика.");
    }

    @Test
    public void testUpdateEpicFields() {
        Epic epic = new Epic(3, "Epic Title", "Epic Description");

        // Создаём подзадачи
        Subtask subtask1 = new Subtask(301, "Subtask 1", "Description 1", Status.NEW, epic.getId(), Duration.ofMinutes(30), LocalDateTime.of(2024, 10, 15, 9, 0));
        Subtask subtask2 = new Subtask(302, "Subtask 2", "Description 2", Status.DONE, epic.getId(), Duration.ofMinutes(45), LocalDateTime.of(2024, 10, 15, 10, 0));

        // Добавляем подзадачи в эпик
        epic.addSubtask(subtask1.getId());
        epic.addSubtask(subtask2.getId());

        // Обновляем поля эпика на основе подзадач
        epic.updateFields(Arrays.asList(subtask1, subtask2));

        // Проверяем статус эпика
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус эпика должен быть IN_PROGRESS, так как подзадачи имеют разные статусы.");

        // Проверяем общую продолжительность
        assertEquals(Duration.ofMinutes(75), epic.getDuration(), "Общая продолжительность эпика должна быть суммой длительностей подзадач.");

        // Проверяем startTime и endTime
        assertEquals(LocalDateTime.of(2024, 10, 15, 9, 0), epic.getStartTime(), "StartTime эпика должен быть минимальным startTime подзадач.");
        assertEquals(LocalDateTime.of(2024, 10, 15, 10, 45), epic.getEndTime(), "EndTime эпика должен быть максимальным endTime подзадач.");
    }
}
