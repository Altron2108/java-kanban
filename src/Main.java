public class Main {
    public static void main(String[] args) {
        // Получение экземпляра TaskManager через Managers
        TaskManager taskManager = Managers.getDefaultTaskManager();

        // Создание эпиков и подзадач
        Epic epic1 = new Epic("Epic 1", "Description of Epic 1");
        Epic epic2 = new Epic("Epic 2", "Description of Epic 2");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask 1", "Description of Subtask 1", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description of Subtask 2", Status.NEW, epic1.getId());
        Subtask subtask3 = new Subtask("Subtask 3", "Description of Subtask 3", Status.NEW, epic2.getId());

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        // Получение задач, эпиков и подзадач по ID
        System.out.println(taskManager.getTaskById(subtask1.getId()));
        System.out.println(taskManager.getEpicById(epic1.getId()));
        System.out.println(taskManager.getSubtaskById(subtask1.getId()));

        // Обновление эпика
        epic1.setTitle("Updated Epic 1");
        taskManager.updateEpic(epic1);

        // Обновление статусов задач и подзадач
        subtask1.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);

        subtask2.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask2);

        System.out.println(taskManager.getEpicById(epic1.getId()));

        // Удаление всех задач, эпиков и подзадач
        taskManager.removeAllTasks();
        taskManager.removeAllEpics();
        taskManager.removeAllSubtasks();

        // Удаление задачи и эпика по ID
        taskManager.deleteTaskById(subtask1.getId());
        taskManager.deleteEpicById(epic1.getId());
        taskManager.deleteSubtaskById(subtask1.getId());

        // Печать истории просмотров через TaskManager
        System.out.println("History:");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }
}
