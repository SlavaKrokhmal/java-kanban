package handlers;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;

import java.io.IOException;

public class PrioritizedTasksHandler extends BaseHttpHandler {
    public PrioritizedTasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void handleRequest(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            String response = gson.toJson(taskManager.getPrioritizedTasks());
            sendResponse(exchange, response, 200);
        } else {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
    }
}
