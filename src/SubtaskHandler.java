import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public SubtaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if ("GET".equalsIgnoreCase(method)) {
            handleGetSubtasks(exchange);
        } else if ("POST".equalsIgnoreCase(method)) {
            handlePostSubtask(exchange);
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        List<Subtask> subtasks = taskManager.getSubtasks();
        String response = gson.toJson(subtasks);
        sendText(exchange, response, 200);
    }

    private void handlePostSubtask(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes());
        Subtask subtask = gson.fromJson(body, Subtask.class);

        if (subtask == null || subtask.getTitle() == null || subtask.getDescription() == null) {
            sendText(exchange, "Invalid subtask data", 400);
            return;
        }

        try {
            taskManager.createSubtask(subtask);
            sendText(exchange, "Subtask created successfully", 201);
        } catch (TaskTimeConflictException e) {
            sendHasInteractions(exchange);
        }
    }
}
