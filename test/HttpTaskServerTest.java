import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    private HttpTaskServer server;
    private TaskManager taskManager;
    private final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = GsonProvider.createGson(); // Используем Gson с адаптерами

    @BeforeEach
    public void setUp() throws IOException {
        taskManager = new InMemoryTaskManager();
        server = new HttpTaskServer(taskManager);
        server.start();
    }

    @AfterEach
    public void tearDown() {
        server.stop();
    }

    @Test
    public void shouldAddTaskSuccessfully() {
        RegularTask task = new RegularTask("Test Task",
                "Description", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        String taskJson = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .header("Content-Type", "application/json") // Указываем заголовок
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());

            List<Task> tasks = taskManager.getTasks();
            assertNotNull(tasks);
            assertEquals(1, tasks.size());
            assertEquals(task.getTitle(), tasks.getFirst().getTitle()); // Проверка заголовка задачи
        } catch (IOException | InterruptedException e) {
            fail("Exception occurred: " + e.getMessage(), e);
        } catch (JsonParseException e) {
            fail("Error parsing JSON: " + e.getMessage(), e);
        }
    }

    @Test
    public void shouldReturnAllTasksWhenRequested() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();

        try {
            // Первый запрос для получения всех задач
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            // Десериализуем список задач из JSON
            List<RegularTask> tasks = gson.fromJson(response.body(), new TypeToken<List<RegularTask>>() {
            }.getType());
            System.out.println("Initial tasks count: " + tasks.size());
            assertEquals(0, tasks.size()); // Проверяем количество задач

            // Создаем новую задачу
            RegularTask newTask = new RegularTask(
                    "Test Task",
                    "Description",
                    Status.NEW,
                    Duration.ofSeconds(1800),
                    LocalDateTime.parse("2024-10-23T22:30:37.8413828")
            );

            // Добавляем новую задачу в TaskManager
            taskManager.createTask(newTask);
            System.out.println("Task added: " + newTask);

            // Добавляем небольшую задержку, чтобы сервер успел обработать добавление
            Thread.sleep(100); // Задержка 100 миллисекунд

            // Повторный запрос для получения всех задач
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response body after adding task: " + response.body());

            // Проверяем обновленный список задач
            tasks = gson.fromJson(response.body(), new TypeToken<List<RegularTask>>() {
            }.getType());
            System.out.println("Updated tasks count: " + tasks.size());
            assertEquals(1, tasks.size()); // Проверяем, что задача добавлена

            // Проверяем заголовок первой задачи
            assertEquals(newTask.getTitle(), tasks.getFirst().getTitle());

        } catch (IOException | InterruptedException e) {
            fail("Exception occurred: " + e.getMessage(), e);
        }
    }

    @Test
    public void shouldCreateEpicSuccessfully() {
        Epic epic = new Epic("Epic Task", "Description");
        String epicJson = gson.toJson(epic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .header("Content-Type", "application/json") // Указываем заголовок
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());

            List<Epic> epics = taskManager.getEpics();
            assertEquals(1, epics.size());
            assertEquals("Epic Task", epics.getFirst().getTitle());
        } catch (IOException | InterruptedException e) {
            fail("Exception occurred: " + e.getMessage(), e);
        }
    }


    @Test
    public void shouldReturnPrioritizedTasks() throws IOException, InterruptedException {
        RegularTask task1 = new RegularTask("Task 1", "Description",
                Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        RegularTask task2 = new RegularTask("Task 2", "Description",
                Status.NEW, Duration.ofMinutes(60), LocalDateTime.now().plusHours(1));

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        // Выводим тело ответа для проверки
        System.out.println("Response body: " + response.body());

        // Пробуем десериализовать ответ
        Type taskListType = new TypeToken<List<RegularTask>>() {
        }.getType();
        List<RegularTask> prioritizedTasks = gson.fromJson(response.body(), taskListType);

        assertNotNull(prioritizedTasks);  // Проверяем, что список не пустой
        assertEquals(2, prioritizedTasks.size());
        assertEquals("Task 1", prioritizedTasks.getFirst().getTitle());
        // Task 1 должен быть первым по приоритету
    }


    @Test
    public void shouldReturnEmptyHistoryInitially() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/history"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> history = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());
        assertEquals(0, history.size());
    }
}
