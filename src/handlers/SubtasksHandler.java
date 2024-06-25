package handlers;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import java.io.IOException;

public class SubtasksHandler extends BaseHttpHandler {
    public SubtasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void handleRequest(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if ("GET".equals(method) && "/subtasks".equals(path)) {
            handleGetAllSubtasks(exchange);
        } else if ("DELETE".equals(method) && path.matches("/subtasks/\\d+")) {
            handleDeleteSubtaskById(exchange);
        } else {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
    }

    private void handleGetAllSubtasks(HttpExchange exchange) throws IOException {
        String response = gson.toJson(taskManager.getAllSubtasks());
        sendResponse(exchange, response, 200);
    }

    private void handleDeleteSubtaskById(HttpExchange exchange) throws IOException {
        int id = extractId(exchange.getRequestURI().getPath());
        taskManager.deleteSubtask(id);
        sendResponse(exchange, "Подзадача удалена", 200);
    }
}
