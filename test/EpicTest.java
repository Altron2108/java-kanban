import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {
    private InMemoryTaskManager taskManager;
    private Epic epic;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
        epic = new Epic("Epic 1", "Description of Epic 1");
        taskManager.createEpic(epic);
    }

    @Test
    public void testEpicStatusWhenNoSubtasks() {
        assertEquals(Status.NEW, taskManager.getEpicById(epic.getId()).getStatus(),
                "Epic status should be NEW when there are no subtasks");
    }

    @Test
    public void testEpicStatusWithAllNewSubtasks() {
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.now(), epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", Status.NEW,
                Duration.ofMinutes(90), LocalDateTime.now().plusHours(2), epic.getId());

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(Status.NEW, taskManager.getEpicById(epic.getId()).getStatus(),
                "Epic status should be NEW when all subtasks are NEW");
    }

    @Test
    public void testEpicStatusWithAllDoneSubtasks() {
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", Status.DONE,
                Duration.ofMinutes(60), LocalDateTime.now(), epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", Status.DONE,
                Duration.ofMinutes(90), LocalDateTime.now().plusHours(2), epic.getId());

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(Status.DONE, taskManager.getEpicById(epic.getId()).getStatus(),
                "Epic status should be DONE when all subtasks are DONE");
    }

    @Test
    public void testEpicStatusWithNewAndInProgressSubtasks() {
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.now(), epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", Status.IN_PROGRESS,
                Duration.ofMinutes(90), LocalDateTime.now().plusHours(2), epic.getId());

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, taskManager.getEpicById(epic.getId()).getStatus(),
                "Epic status should be IN_PROGRESS when subtasks have mixed statuses");
    }

    @Test
    public void testEpicStatusWithAllInProgressSubtasks() {
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", Status.IN_PROGRESS,
                Duration.ofMinutes(60), LocalDateTime.now(), epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", Status.IN_PROGRESS,
                Duration.ofMinutes(90), LocalDateTime.now().plusHours(2), epic.getId());

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, taskManager.getEpicById(epic.getId()).getStatus(),
                "Epic status should be IN_PROGRESS when all subtasks are IN_PROGRESS");
    }
}

