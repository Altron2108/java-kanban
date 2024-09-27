import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    private FileBackedTaskManager manager;
    private File testFile;

    @BeforeEach
    public void setUp() throws IOException {
        // Создаём временный файл для тестов
        testFile = File.createTempFile("tasks", ".csv");
        manager = new FileBackedTaskManager(testFile);
    }

    @Test
    public void shouldSaveAndLoadEmptyManager() {
        // Проверяем, что при сохранении и загрузке пустого менеджера данные не теряются
        manager.save();
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(testFile);
        assertTrue(loadedManager.getTasks().isEmpty(), "Tasks list should be empty.");
        assertTrue(loadedManager.getEpics().isEmpty(), "Epics list should be empty.");
        assertTrue(loadedManager.getSubtasks().isEmpty(), "Subtasks list should be empty.");
    }

    @Test
    public void shouldSaveAndLoadSingleTask() {
        Task task = new Task("Test Task", "Test Description", Status.NEW);
        int taskId = manager.createTask(task);

        // Сохраняем менеджер и загружаем из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(testFile);

        // Проверяем, что задача была восстановлена корректно
        Task loadedTask = loadedManager.getTaskById(taskId);
        assertNotNull(loadedTask, "Task should be present after loading.");
        assertEquals(task.getTitle(), loadedTask.getTitle(), "Task title should match.");
        assertEquals(task.getDescription(), loadedTask.getDescription(), "Task description should match.");
        assertEquals(task.getStatus(), loadedTask.getStatus(), "Task status should match.");
    }

    @Test
    public void shouldSaveAndLoadSingleEpicWithSubtasks() {
        Epic epic = new Epic("String Epic", "Epic Description");
        int epicId = manager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", Status.NEW, epicId);
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", Status.IN_PROGRESS, epicId);

        int subtaskId1 = manager.createSubtask(subtask1);
        int subtaskId2 = manager.createSubtask(subtask2);

        // Сохраняем менеджер и загружаем из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(testFile);

        // Проверяем, что эпик и его подзадачи восстановлены корректно
        Epic loadedEpic = loadedManager.getEpicById(epicId);
        assertNotNull(loadedEpic, "Epic should be present after loading.");
        assertEquals(epic.getTitle(), loadedEpic.getTitle(), "Epic title should match.");
        assertEquals(epic.getDescription(), loadedEpic.getDescription(), "Epic description should match.");

        List<Integer> subtaskIds = loadedEpic.getSubtaskIds();
        assertEquals(2, subtaskIds.size(), "Epic should have two subtasks.");
        assertTrue(subtaskIds.contains(subtaskId1), "Subtask 1 should be linked to the epic.");
        assertTrue(subtaskIds.contains(subtaskId2), "Subtask 2 should be linked to the epic.");

        Subtask loadedSubtask1 = loadedManager.getSubtaskById(subtaskId1);
        assertNotNull(loadedSubtask1, "Subtask 1 should be present after loading.");
        assertEquals(subtask1.getTitle(), loadedSubtask1.getTitle(), "Subtask 1 title should match.");
        assertEquals(subtask1.getDescription(), loadedSubtask1.getDescription(), "Subtask 1 description should match.");
        assertEquals(subtask1.getStatus(), loadedSubtask1.getStatus(), "Subtask 1 status should match.");

        Subtask loadedSubtask2 = loadedManager.getSubtaskById(subtaskId2);
        assertNotNull(loadedSubtask2, "Subtask 2 should be present after loading.");
        assertEquals(subtask2.getTitle(), loadedSubtask2.getTitle(), "Subtask 2 title should match.");
        assertEquals(subtask2.getDescription(), loadedSubtask2.getDescription(), "Subtask 2 description should match.");
        assertEquals(subtask2.getStatus(), loadedSubtask2.getStatus(), "Subtask 2 status should match.");
    }

    @Test
    public void shouldSaveAndLoadHistory() {
        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.IN_PROGRESS);

        int taskId1 = manager.createTask(task1);
        int taskId2 = manager.createTask(task2);

        // Добавляем задачи в историю
        manager.getTaskById(taskId1);
        manager.getTaskById(taskId2);

        // Сохраняем менеджер и загружаем из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(testFile);

        // Проверяем, что история была восстановлена корректно
        List<Task> history = loadedManager.getHistoryManager().getHistory();
        assertEquals(2, history.size(), "History size should be 2.");
        assertEquals(taskId1, history.get(0).getId(), "First task in history should match.");
        assertEquals(taskId2, history.get(1).getId(), "Second task in history should match.");
    }

    @Test
    public void shouldThrowExceptionWhenSavingToInvalidFile() {
        // Создаём файл в недоступной директории (преднамеренная ошибка)
        File invalidFile = new File("/invalid/path/tasks.csv");
        FileBackedTaskManager invalidManager = new FileBackedTaskManager(invalidFile);

        Task task = new Task("Test Task", "Test Description", Status.NEW);
        assertThrows(ManagerSaveException.class, () -> invalidManager.createTask(task),
                "Saving to an invalid file should throw ManagerSaveException.");
    }
}
