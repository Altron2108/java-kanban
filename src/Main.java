
public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefaultTaskManager();
        HistoryManager historyManager = Managers.getDefaultHistoryManager();

        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW);
        Epic epic = new Epic("Epic 1", "Epic Description");

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 Description", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 Description", Status.NEW, epic.getId());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        System.out.println(historyManager.getHistory());
        taskManager.deleteTaskById(task1.getId());
        System.out.println(historyManager.getHistory());
        taskManager.deleteEpicById(epic.getId());
        System.out.println(historyManager.getHistory());
    }
}