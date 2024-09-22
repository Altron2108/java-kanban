
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void managersShouldReturnInitializedInstances() {
        // Получаем экземпляры менеджеров
        TaskManager taskManager = Managers.getDefaultTaskManager();
        HistoryManager historyManager = Managers.getDefaultHistoryManager();


        assertNotNull(taskManager, "Менеджер задач не инициализирован.");
        assertNotNull(historyManager, "Менеджер истории не инициализирован.");
    }
}
