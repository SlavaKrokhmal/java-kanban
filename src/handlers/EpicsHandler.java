package handlers;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import exceptions.NotFoundException;
import model.Epic;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class EpicsHandler extends BaseHttpHandler {
    public EpicsHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void handleRequest(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if ("GET".equals(method) && "/epics".equals(path)) {
            handleGetAllEpics(exchange);
        } else if ("GET".equals(method) && path.matches("/epics/\\d+")) {
            handleGetEpicById(exchange);
        } else if ("DELETE".equals(method) && path.matches("/epics/\\d+")) {
            handleDeleteEpicById(exchange);
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }

    private void handleGetAllEpics(HttpExchange exchange) throws IOException {
        List<Epic> epics = taskManager.getAllEpics();
        String response = gson.toJson(epics);
        sendResponse(exchange, response, 200);
    }

    private void handleGetEpicById(HttpExchange exchange) throws IOException {
        int id = extractId(exchange.getRequestURI().getPath());
        Epic epic = Optional.ofNullable(taskManager.getEpic(id)).orElseThrow(() -> new NotFoundException("Эпик не найден"));
        String response = gson.toJson(epic);
        sendResponse(exchange, response, 200);
    }

    private void handleDeleteEpicById(HttpExchange exchange) throws IOException {
        int id = extractId(exchange.getRequestURI().getPath());
        taskManager.deleteEpic(id);
        sendResponse(exchange, "Эпик удален", 200);
    }
}
