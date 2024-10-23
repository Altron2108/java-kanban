import java.time.Duration;
import java.time.LocalDateTime;


public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefaultTaskManager();
        HistoryManager historyManager = Managers.getDefaultHistoryManager();

        // Создаем обычные задачи с использованием подкласса RegularTask
        Task task1 = new RegularTask("Task 1", "Description 1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.now());
        Task task2 = new RegularTask("Task 2", "Description 2", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(60));

        Epic epic = new Epic("Epic 1", "Epic Description");

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic);

        // Создаем подзадачи с длительностью и временем начала
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 Description", Status.NEW,
                Duration.ofMinutes(90), LocalDateTime.now(), epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 Description", Status.NEW,
                Duration.ofMinutes(45), LocalDateTime.now().plusMinutes(100), epic.getId());

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        // Отображаем историю
        System.out.println(historyManager.getHistory());

        // Удаляем задачу и эпик, проверяем историю
        taskManager.deleteTaskById(task1.getId());
        System.out.println(historyManager.getHistory());

        taskManager.deleteEpicById(epic.getId());
        System.out.println(historyManager.getHistory());
    }
}