import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InMemoryTaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void createTask_TaskIsCreatedSuccessfully() {
        Task task = new Task("Test Task", "Test Description", Status.NEW);
        int taskId = taskManager.createTask(task);
        Task retrievedTask = taskManager.getTaskById(taskId);
        assertNotNull(retrievedTask);
        assertEquals(taskId, retrievedTask.getId());
    }

    @Test
    public void getTaskById_TaskIsRetrievedSuccessfully() {
        Task task = new Task("Test Task", "Test Description", Status.NEW);
        int taskId = taskManager.createTask(task);
        Task retrievedTask = taskManager.getTaskById(taskId);
        assertNotNull(retrievedTask);
        assertEquals(taskId, retrievedTask.getId());
    }

    @Test
    public void updateTask_TaskIsUpdatedSuccessfully() {
        Task task = new Task("Test Task", "Test Description", Status.NEW);
        int taskId = taskManager.createTask(task);
        task.setTitle("Updated Task");
        taskManager.updateTask(task);
        Task retrievedTask = taskManager.getTaskById(taskId);
        assertEquals("Updated Task", retrievedTask.getTitle());
    }

    @Test
    public void deleteTaskById_TaskIsDeletedSuccessfully() {
        Task task = new Task("Test Task", "Test Description", Status.NEW);
        int taskId = taskManager.createTask(task);
        taskManager.deleteTaskById(taskId);
        Task retrievedTask = taskManager.getTaskById(taskId);
        assertNull(retrievedTask);
    }

    @Test
    public void createSubtask_SubtaskIsCreatedSuccessfully() {
        Epic epic = new Epic("Epic Title", "Epic Description");
        int epicId = taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask Title", "Subtask Description", Status.NEW, epicId);
        int subtaskId = taskManager.createSubtask(subtask);
        assertNotNull(taskManager.getSubtaskById(subtaskId));
    }

    @Test
    public void updateSubtask_SubtaskIsUpdatedSuccessfully() {
        Epic epic = new Epic("Epic Title", "Epic Description");
        int epicId = taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask Title", "Subtask Description", Status.NEW, epicId);
        int subtaskId = taskManager.createSubtask(subtask);
        subtask.setTitle("Updated Subtask Title");
        taskManager.updateSubtask(subtask);
        Subtask retrievedSubtask = taskManager.getSubtaskById(subtaskId);
        assertEquals("Updated Subtask Title", retrievedSubtask.getTitle());
    }

    @Test
    public void deleteSubtaskById_SubtaskIsDeletedSuccessfully() {
        Epic epic = new Epic("Epic Title", "Epic Description");
        int epicId = taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask Title", "Subtask Description", Status.NEW, epicId);
        int subtaskId = taskManager.createSubtask(subtask);
        taskManager.deleteSubtaskById(subtaskId);
        assertNull(taskManager.getSubtaskById(subtaskId));
    }

    @Test
    public void epicStatusUpdate_EpicStatusUpdatesCorrectly() {
        Epic epic = new Epic("Epic Title", "Epic Description");
        int epicId = taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", Status.NEW, epicId);
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", Status.DONE, epicId);
        taskManager.createSubtask(subtask2);
        taskManager.updateSubtask(subtask2); // Should update epic status to IN_PROGRESS
        assertEquals(Status.IN_PROGRESS, taskManager.getEpicById(epicId).getStatus());
    }
}