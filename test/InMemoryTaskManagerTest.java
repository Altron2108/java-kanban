import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void setUp() {
        manager = new InMemoryTaskManager();
    }

    @Test
    public void testAddTaskWithDurationAndStartTime() {
        Task task = new Task(0, "Task 1", "Description 1", Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.of(2024, 10, 15, 9, 0));
        manager.addTask(task);
        Task retrieved = manager.getTask(task.id);
        assertNotNull(retrieved);
        assertEquals(Duration.ofMinutes(60), retrieved.getDuration());
        assertEquals(LocalDateTime.of(2024, 10, 15, 9, 0), retrieved.getStartTime());
        assertEquals(LocalDateTime.of(2024, 10, 15, 10, 0), retrieved.getEndTime());
    }

    @Test
    public void testEpicStatusInProgress() {
        Epic epic = new Epic(0, "Epic 3", "Epic Description");
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask(0, "Subtask 5", "Sub Description 5", Status.NEW, epic.id,
                Duration.ofMinutes(25), LocalDateTime.of(2024, 10, 17, 9, 0));
        Subtask subtask2 = new Subtask(0, "Subtask 6", "Sub Description 6", Status.IN_PROGRESS,
                epic.id, Duration.ofMinutes(35), LocalDateTime.of(2024, 10, 17, 10, 0));
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        Epic updatedEpic = manager.getEpic(epic.id);
        assertEquals(Status.IN_PROGRESS, updatedEpic.getStatus());
        assertEquals(Duration.ofMinutes(60), updatedEpic.getDuration());
        assertEquals(LocalDateTime.of(2024, 10, 17, 9, 0), updatedEpic.getStartTime());
        assertEquals(LocalDateTime.of(2024, 10, 17, 10, 35), updatedEpic.getEndTime());
    }

    @Test
    public void testPrioritizedTasksOrder() {
        Task task1 = new Task(0, "Task A", "Description A", Status.NEW, Duration.ofMinutes(30),
                LocalDateTime.of(2024, 10, 18, 9, 0));
        Task task2 = new Task(0, "Task B", "Description B", Status.NEW, Duration.ofMinutes(45),
                LocalDateTime.of(2024, 10, 18, 10, 0));
        Task task3 = new Task(0, "Task C", "Description C", Status.NEW, Duration.ofMinutes(60),
                null); // Без startTime
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        List<Task> prioritized = manager.getPrioritizedTasks();
        assertEquals(2, prioritized.size());
        assertEquals(task1, prioritized.get(0));
        assertEquals(task2, prioritized.get(1));
    }

    @Test
    public void testAddOverlappingTask() {
        Task task1 = new Task(0, "Task D", "Description D", Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.of(2024, 10, 19, 9, 0));
        Task task2 = new Task(0, "Task E", "Description E", Status.NEW, Duration.ofMinutes(30),
                LocalDateTime.of(2024, 10, 19, 9, 30));
        manager.addTask(task1);
        assertThrows(ManagerSaveException.class, () -> manager.addTask(task2), "Задача пересекается с существующими задачами.");
    }

    @Test
    public void testEpicStatusAllNew() {
        Epic epic = new Epic(0, "Epic All New", "All subtasks are NEW");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask(0, "Subtask 1", "Description 1", Status.NEW, epic.id,
                Duration.ofMinutes(30), LocalDateTime.of(2024, 10, 15, 9, 0));
        Subtask subtask2 = new Subtask(0, "Subtask 2", "Description 2", Status.NEW, epic.id,
                Duration.ofMinutes(45), LocalDateTime.of(2024, 10, 15, 10, 0));
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        Epic updatedEpic = manager.getEpic(epic.id);
        assertEquals(Status.NEW, updatedEpic.getStatus(), "Эпик должен иметь статус NEW, если все подзадачи NEW.");
    }

    @Test
    public void testEpicStatusAllDone() {
        Epic epic = new Epic(0, "Epic All Done", "All subtasks are DONE");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask(0, "Subtask 3", "Description 3", Status.DONE, epic.id,
                Duration.ofMinutes(20), LocalDateTime.of(2024, 10, 16, 9, 0));
        Subtask subtask2 = new Subtask(0, "Subtask 4", "Description 4", Status.DONE, epic.id,
                Duration.ofMinutes(40), LocalDateTime.of(2024, 10, 16, 10, 0));
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        Epic updatedEpic = manager.getEpic(epic.id);
        assertEquals(Status.DONE, updatedEpic.getStatus(), "Эпик должен иметь статус DONE, если все подзадачи DONE.");
    }

    @Test
    public void testEpicStatusInProgressWithMixedSubtasks() {
        Epic epic = new Epic(0, "Epic In Progress", "Mixed statuses of subtasks");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask(0, "Subtask 5", "Description 5", Status.NEW, epic.id,
                Duration.ofMinutes(25), LocalDateTime.of(2024, 10, 17, 9, 0));
        Subtask subtask2 = new Subtask(0, "Subtask 6", "Description 6", Status.IN_PROGRESS, epic.id,
                Duration.ofMinutes(35), LocalDateTime.of(2024, 10, 17, 10, 0));
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        Epic updatedEpic = manager.getEpic(epic.id);
        assertEquals(Status.IN_PROGRESS, updatedEpic.getStatus(), "Эпик должен иметь статус IN_PROGRESS, " +
                "если есть подзадачи IN_PROGRESS.");
    }

    @Test
    public void testEpicDurationAndTimeCalculation() {
        Epic epic = new Epic(0, "Epic Time Calculation", "Check duration and time calculations");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask(0, "Subtask 7", "Description 7", Status.NEW, epic.id,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 10, 18, 9, 0));
        Subtask subtask2 = new Subtask(0, "Subtask 8", "Description 8", Status.NEW, epic.id,
                Duration.ofMinutes(30), LocalDateTime.of(2024, 10, 18, 9, 15));
        Subtask subtask3 = new Subtask(0, "Subtask 9", "Description 9", Status.NEW, epic.id,
                Duration.ofMinutes(45), LocalDateTime.of(2024, 10, 18, 10, 0));
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);

        Epic updatedEpic = manager.getEpic(epic.id);
        assertEquals(Duration.ofMinutes(90), updatedEpic.getDuration(), "Суммарная продолжительность " +
                "эпика должна быть 90 минут.");
        assertEquals(LocalDateTime.of(2024, 10, 18, 9, 0), updatedEpic.getStartTime(),
                "StartTime эпика должен быть минимальным startTime подзадач.");
        assertEquals(LocalDateTime.of(2024, 10, 18, 10, 45), updatedEpic.getEndTime(),
                "EndTime эпика должен быть максимальным endTime подзадач.");
    }

    @Test
    public void testRemovingSubtaskUpdatesEpic() {
        Epic epic = new Epic(0, "Epic Remove Subtask", "Check epic update after removing subtask");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask(0, "Subtask 10", "Description 10", Status.NEW, epic.id,
                Duration.ofMinutes(30), LocalDateTime.of(2024, 10, 19, 9, 0));
        Subtask subtask2 = new Subtask(0, "Subtask 11", "Description 11", Status.DONE, epic.id,
                Duration.ofMinutes(30), LocalDateTime.of(2024, 10, 19, 10, 0));
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        // Эпик должен иметь статус IN_PROGRESS
        Epic updatedEpic = manager.getEpic(epic.id);
        assertEquals(Status.IN_PROGRESS, updatedEpic.getStatus(), "Эпик должен иметь статус IN_PROGRESS.");

        // Удалим одну подзадачу
        manager.removeSubtask(subtask2.id);

        updatedEpic = manager.getEpic(epic.id);
        assertEquals(Status.NEW, updatedEpic.getStatus(), "После удаления DONE подзадачи эпик должен иметь " +
                "статус NEW.");
        assertEquals(Duration.ofMinutes(30), updatedEpic.getDuration(), "Эпик должен иметь продолжительность " +
                "30 минут после удаления подзадачи.");
        assertEquals(LocalDateTime.of(2024, 10, 19, 9, 0), updatedEpic.getStartTime(),
                "StartTime эпика должен оставаться прежним.");
        assertEquals(LocalDateTime.of(2024, 10, 19, 9, 30), updatedEpic.getEndTime(),
                "EndTime эпика должен обновиться.");
    }

    @Test
    public void testAddingSubtaskToNonexistentEpic() {
        Subtask subtask = new Subtask(0, "Subtask 12", "Description 12", Status.NEW, 999,
                Duration.ofMinutes(30), LocalDateTime.of(2024, 10, 20, 9, 0));
        assertThrows(ManagerSaveException.class, () -> manager.addSubtask(subtask),
                "Добавление подзадачи к несуществующему эпику должно вызывать ManagerLoadException.");
    }

    @Test
    public void testEpicTimeWithoutStartTime() {
        Epic epic = new Epic(0, "Epic No Start Time", "Epic with some subtasks without startTime");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask(0, "Subtask 13", "Description 13", Status.NEW, epic.id,
                Duration.ofMinutes(30), null);
        Subtask subtask2 = new Subtask(0, "Subtask 14", "Description 14", Status.NEW, epic.id,
                Duration.ofMinutes(45), LocalDateTime.of(2024, 10, 21, 10, 0));
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        Epic updatedEpic = manager.getEpic(epic.id);
        assertEquals(Status.NEW, updatedEpic.getStatus(), "Эпик должен иметь статус NEW.");
        assertEquals(Duration.ofMinutes(75), updatedEpic.getDuration(), "Суммарная продолжительность " +
                "должна быть 75 минут.");
        assertEquals(LocalDateTime.of(2024, 10, 21, 10, 0),
                updatedEpic.getStartTime(), "StartTime эпика должен быть минимальным startTime подзадач.");
        assertEquals(LocalDateTime.of(2024, 10, 21, 10, 45),
                updatedEpic.getEndTime(), "EndTime эпика должен быть максимальным endTime подзадач.");
    }

    @Test
    public void testAddNonOverlappingTasks() {
        Task task1 = new Task(0, "Task Non-Overlap 1", "Description 1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2024, 10, 22, 9, 0));
        Task task2 = new Task(0, "Task Non-Overlap 2", "Description 2", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2024, 10, 22, 10, 0));
        Task task3 = new Task(0, "Task Non-Overlap 3", "Description 3", Status.NEW,
                Duration.ofMinutes(45), LocalDateTime.of(2024, 10, 22, 10, 30));

        assertDoesNotThrow(() -> manager.addTask(task1),
                "Добавление первой задачи должно проходить без ошибок.");
        assertDoesNotThrow(() -> manager.addTask(task2),
                "Добавление второй задачи должно проходить без ошибок.");
        assertDoesNotThrow(() -> manager.addTask(task3),
                "Добавление третьей задачи должно проходить без ошибок.");
    }

    @Test
    public void testAddOverlappingTasks() {
        Task task1 = new Task(0, "Task Overlap 1", "Description 1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2024, 10, 23, 9, 0));
        Task task2 = new Task(0, "Task Overlap 2", "Description 2", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2024, 10, 23, 9, 30));
        // Пересекается с task1
        Task task3 = new Task(0, "Task Overlap 3", "Description 3", Status.NEW,
                Duration.ofMinutes(45), LocalDateTime.of(2024, 10, 23, 10, 0));
        // Не пересекается

        manager.addTask(task1);
        assertThrows(ManagerSaveException.class, () -> manager.addTask(task2),
                "Добавление пересекающейся задачи должно вызывать ManagerLoadException.");
        assertDoesNotThrow(() -> manager.addTask(task3),
                "Добавление непересекающейся задачи должно проходить без ошибок.");
    }

    @Test
    public void testAddSubtasksWithOverlappingTimes() {
        Epic epic = new Epic(0, "Epic Overlap Subtasks", "Epic with overlapping subtasks");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask(0, "Subtask Overlap 1", "Description 1", Status.NEW, epic.id,
                Duration.ofMinutes(30), LocalDateTime.of(2024, 10, 24, 9, 0));
        Subtask subtask2 = new Subtask(0, "Subtask Overlap 2", "Description 2", Status.NEW, epic.id,
                Duration.ofMinutes(45), LocalDateTime.of(2024, 10, 24, 9, 15));
        // Пересекается с subtask1
        Subtask subtask3 = new Subtask(0, "Subtask Overlap 3", "Description 3", Status.NEW, epic.id,
                Duration.ofMinutes(30), LocalDateTime.of(2024, 10, 24, 10, 0));
        // Не пересекается

        manager.addSubtask(subtask1);
        assertThrows(ManagerSaveException.class, () -> manager.addSubtask(subtask2),
                "Добавление пересекающейся подзадачи должно вызывать ManagerLoadException.");
        assertDoesNotThrow(() -> manager.addSubtask(subtask3),
                "Добавление непересекающейся подзадачи должно проходить без ошибок.");
    }

    @Test
    public void testAddTaskWithoutStartTime() {
        Task task1 = new Task(0, "Task No Start 1", "Description 1", Status.NEW,
                Duration.ofMinutes(60), null);
        Task task2 = new Task(0, "Task No Start 2", "Description 2", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2024, 10, 25, 10, 0));

        assertDoesNotThrow(() -> manager.addTask(task1), "Добавление задачи без startTime должно проходить " +
                "без ошибок.");
        assertDoesNotThrow(() -> manager.addTask(task2), "Добавление задачи с startTime должно проходить " +
                "без ошибок.");
    }

    @Test
    public void testRemoveTask() {
        Task task = new Task(0, "Task Remove", "Description Remove", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2024, 10, 31, 9, 0));
        manager.addTask(task);
        assertNotNull(manager.getTask(task.id), "Задача должна быть добавлена.");

        manager.removeTask(task.id);
        assertNull(manager.getTask(task.id), "Задача должна быть удалена.");
    }

    @Test
    public void testRemoveEpicAlsoRemovesSubtasks() {
        Epic epic = new Epic(0, "Epic Remove", "Epic Description");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask(0, "Subtask Remove 1", "Description 1", Status.NEW, epic.id,
                Duration.ofMinutes(30), LocalDateTime.of(2024, 11, 1, 9, 0));
        Subtask subtask2 = new Subtask(0, "Subtask Remove 2", "Description 2", Status.NEW, epic.id,
                Duration.ofMinutes(45), LocalDateTime.of(2024, 11, 1, 10, 0));
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        assertNotNull(manager.getEpic(epic.id), "Эпик должен быть добавлен.");
        assertNotNull(manager.getSubtask(subtask1.id), "Подзадача 1 должна быть добавлена.");
        assertNotNull(manager.getSubtask(subtask2.id), "Подзадача 2 должна быть добавлена.");

        manager.removeEpic(epic.id);
        assertNull(manager.getEpic(epic.id), "Эпик должен быть удален.");
        assertNull(manager.getSubtask(subtask1.id), "Подзадача 1 должна быть удалена вместе с эпиком.");
        assertNull(manager.getSubtask(subtask2.id), "Подзадача 2 должна быть удалена вместе с эпиком.");
    }
}

