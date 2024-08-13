import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class InMemoryTaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = Managers.getDefaultTaskManager();
    }

    @Test
    public void testCreateTask() {
        Task task = new Task("Test Task", "Test Description", Status.NEW);
        int taskId = taskManager.createTask(task);
        Task retrievedTask = taskManager.getTaskById(taskId);
        assertNotNull(retrievedTask);
        assertEquals(task.getId(), retrievedTask.getId());
        assertEquals(task.getTitle(), retrievedTask.getTitle());
        assertEquals(task.getDescription(), retrievedTask.getDescription());
        assertEquals(task.getStatus(), retrievedTask.getStatus());
    }

    @Test
    public void testGetTaskById() {
        Task task = new Task("Test Task", "Test Description", Status.NEW);
        int taskId = taskManager.createTask(task);
        Task retrievedTask = taskManager.getTaskById(taskId);
        assertNotNull(retrievedTask);
        assertEquals(task.getId(), retrievedTask.getId());
    }

    @Test
    public void testUpdateTask() {
        Task task = new Task("Test Task", "Test Description", Status.NEW);
        int taskId = taskManager.createTask(task);

        // Получаем задачу, которую нужно обновить
        Task updatedTask = taskManager.getTaskById(taskId);
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Description");
        updatedTask.setStatus(Status.IN_PROGRESS);

        taskManager.updateTask(updatedTask);
        Task retrievedTask = taskManager.getTaskById(taskId);
        assertEquals("Updated Task", retrievedTask.getTitle());
        assertEquals("Updated Description", retrievedTask.getDescription());
        assertEquals(Status.IN_PROGRESS, retrievedTask.getStatus());
    }

    @Test
    public void testDeleteTaskById() {
        Task task = new Task("Test Task", "Test Description", Status.NEW);
        int taskId = taskManager.createTask(task);
        taskManager.deleteTaskById(taskId);
        Task retrievedTask = taskManager.getTaskById(taskId);
        assertNull(retrievedTask);
    }

    @Test
    public void testRemoveAllTasks() {
        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.removeAllTasks();
        List<Task> tasks = taskManager.getTasks();
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void testCreateSubtask() {
        Epic epic = new Epic("Epic Title", "Epic Description");
        int epicId = taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask Title", "Subtask Description", Status.NEW, epicId);
        taskManager.createSubtask(subtask);
        Subtask retrievedSubtask = taskManager.getSubtaskById(subtask.getId());
        assertNotNull(retrievedSubtask);
        assertEquals(subtask.getId(), retrievedSubtask.getId());
        assertEquals(subtask.getEpicId(), retrievedSubtask.getEpicId());
    }

    @Test
    public void testGetEpicById() {
        Epic epic = new Epic("Epic Title", "Epic Description");
        int epicId = taskManager.createEpic(epic);
        Epic retrievedEpic = taskManager.getEpicById(epicId);
        assertNotNull(retrievedEpic);
        assertEquals(epicId, retrievedEpic.getId());
        assertEquals(epic.getTitle(), retrievedEpic.getTitle());
        assertEquals(epic.getDescription(), retrievedEpic.getDescription());
    }

    @Test
    public void testUpdateEpic() {
        Epic epic = new Epic("Epic Title", "Epic Description");
        int epicId = taskManager.createEpic(epic);

        // Получаем эпик, который нужно обновить
        Epic updatedEpic = taskManager.getEpicById(epicId);
        updatedEpic.setTitle("Updated Epic Title");
        updatedEpic.setDescription("Updated Epic Description");

        taskManager.updateEpic(updatedEpic);
        Epic retrievedEpic = taskManager.getEpicById(epicId);
        assertEquals("Updated Epic Title", retrievedEpic.getTitle());
        assertEquals("Updated Epic Description", retrievedEpic.getDescription());
    }

    @Test
    public void testDeleteEpicById() {
        Epic epic = new Epic("Epic Title", "Epic Description");
        int epicId = taskManager.createEpic(epic);
        taskManager.deleteEpicById(epicId);
        Epic retrievedEpic = taskManager.getEpicById(epicId);
        assertNull(retrievedEpic);
    }

    @Test
    public void testRemoveAllEpics() {
        Epic epic1 = new Epic("Epic 1", "Description 1");
        Epic epic2 = new Epic("Epic 2", "Description 2");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.removeAllEpics();
        List<Epic> epics = taskManager.getEpics();
        assertTrue(epics.isEmpty());
    }

    @Test
    public void testRemoveAllSubtasks() {
        Epic epic = new Epic("Epic Title", "Epic Description");
        int epicId = taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", Status.NEW, epicId);
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", Status.NEW, epicId);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.removeAllSubtasks();
        List<Subtask> subtasks = taskManager.getSubtasks();
        assertTrue(subtasks.isEmpty());
    }
}
