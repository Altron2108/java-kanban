import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public EpicHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if ("GET".equalsIgnoreCase(method)) {
            handleGetEpics(exchange);
        } else if ("POST".equalsIgnoreCase(method)) {
            handlePostEpic(exchange);
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        List<Epic> epics = taskManager.getEpics();
        String response = gson.toJson(epics);
        sendText(exchange, response, 200);
    }

    private void handlePostEpic(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes());
        Epic epic = gson.fromJson(body, Epic.class);

        if (epic == null || epic.getTitle() == null) {
            sendText(exchange, "Invalid epic data", 400);
            return;
        }

        taskManager.createEpic(epic);
        sendText(exchange, "Epic created successfully", 201);
    }
}
