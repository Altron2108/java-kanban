import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    private static final String FILE_PATH = "test_tasks.csv";
    private FileBackedTaskManager manager;

    @BeforeEach
    void setUp() {
        manager = new FileBackedTaskManager(FILE_PATH);
    }

    @AfterEach
    void tearDown() {
        // Удаляем тестовый файл после каждого теста
        File file = new File(FILE_PATH);
        if (file.exists()) {
            if (!file.delete()) {
                System.err.println("Не удалось удалить тестовый файл: " + FILE_PATH);
            }
        }
    }

    @Test
    void testLoadInvalidFileFormat() {
        // Создание файла с некорректным форматом
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("Неправильный заголовок\n");
            writer.write("Некорректные данные\n");
        } catch (IOException e) {
            fail("Не удалось создать файл для теста: " + e.getMessage());
        }

        FileBackedTaskManager loadedManager = new FileBackedTaskManager(FILE_PATH);
        Exception exception = assertThrows(ManagerSaveException.class, loadedManager::load);
        assertEquals("Некорректный формат файла.", exception.getMessage());
    }
}
