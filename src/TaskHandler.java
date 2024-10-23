import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

public class TaskHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public TaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if ("GET".equalsIgnoreCase(method)) {
            handleGetTasks(exchange);
        } else if ("POST".equalsIgnoreCase(method)) {
            handlePostTask(exchange);
        } else {
            exchange.sendResponseHeaders(405, -1); // Метод не разрешён
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        List<Task> tasks = taskManager.getTasks();
        String response = gson.toJson(tasks);
        sendText(exchange, response, 200);
    }

    private void handlePostTask(HttpExchange exchange) throws IOException {
        InputStreamReader reader = new InputStreamReader(exchange.getRequestBody());
        RegularTask task = gson.fromJson(reader, RegularTask.class);

        if (task == null || task.getTitle() == null || task.getDescription() == null) {
            sendText(exchange, "Invalid task data", 400);
            return;
        }

        try {
            taskManager.addTask(task);  // Предполагается, что у вас есть метод для добавления задачи
            exchange.sendResponseHeaders(201, -1); // Успешное создание
        } catch (TaskTimeConflictException e) {
            sendHasInteractions(exchange); // Метод для обработки конфликта
        } finally {
            exchange.close();
        }
    }

    // Метод для отправки текста в ответ
    private void sendText(HttpExchange exchange, String text, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, text.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(text.getBytes());
        }
    }

    // Метод для обработки ситуации с конфликтами
    private void sendHasInteractions(HttpExchange exchange) throws IOException {
        sendText(exchange, "Task time conflict", 409); // 409 Conflict
    }
}
