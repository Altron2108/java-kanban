import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefaultTaskManager_TaskManagerIsInitialized() {
        // Получаем экземпляр менеджера задач
        TaskManager taskManager = Managers.getDefaultTaskManager();

        assertNotNull(taskManager, "Менеджер задач должен быть инициализирован.");
    }

    @Test
    void getDefaultHistoryManager_HistoryManagerIsInitialized() {
        // Получаем экземпляр менеджера истории
        HistoryManager historyManager = Managers.getDefaultHistoryManager();

        assertNotNull(historyManager, "Менеджер истории должен быть инициализирован.");
    }
}
