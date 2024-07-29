public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        // Создание задач
        Task task1 = new Task("Task 1", "Description of Task 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description of Task 2", Status.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        // Использование методов getTitle() и getDescription()
        System.out.println("Title of task1: " + task1.getTitle());
        System.out.println("Description of task1: " + task1.getDescription());

        // Создание эпиков и подзадач
        Epic epic1 = new Epic("Epic 1", "Description of Epic 1");
        taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask 1", "Description of Subtask 1", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description of Subtask 2", Status.NEW, epic1.getId());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Epic epic2 = new Epic("Epic 2", "Description of Epic 2");
        taskManager.createEpic(epic2);

        Subtask subtask3 = new Subtask("Subtask 3", "Description of Subtask 3", Status.NEW, epic2.getId());
        taskManager.createSubtask(subtask3);

        // Получение задач, эпиков и подзадач по ID
        System.out.println(taskManager.getTaskById(task1.getId()));
        System.out.println(taskManager.getEpicById(epic1.getId()));
        System.out.println(taskManager.getSubtaskById(subtask1.getId())); // Демонстрация использования метода getSubtaskById

        // Обновление эпика
        epic1.setTitle("Updated Epic 1");
        taskManager.updateEpic(epic1);

        // Обновление статусов задач и подзадач
        task1.setStatus(Status.IN_PROGRESS);
        taskManager.createTask(task1);

        subtask1.setStatus(Status.DONE);
        taskManager.createSubtask(subtask1);

        subtask2.setStatus(Status.IN_PROGRESS);
        taskManager.createSubtask(subtask2);

        System.out.println(taskManager.getEpicById(epic1.getId()));

        // Удаление всех задач, эпиков и подзадач
        taskManager.removeAllTasks();
        taskManager.removeAllEpics();
        taskManager.removeAllSubtasks();

        // Удаление задачи и эпика по ID
        taskManager.deleteTaskById(task1.getId());
        taskManager.deleteEpicById(epic1.getId());
        taskManager.deleteSubtaskById(subtask1.getId());
    }
}
