package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import exceptions.NotFoundException;
import model.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class TasksHandler extends BaseHttpHandler {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, (com.google.gson.JsonDeserializer<LocalDateTime>) (json, type, context) -> LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .registerTypeAdapter(LocalDateTime.class, (com.google.gson.JsonSerializer<LocalDateTime>) (src, type, context) -> context.serialize(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
            .registerTypeAdapter(Duration.class, (com.google.gson.JsonDeserializer<Duration>) (json, type, context) -> Duration.ofMinutes(json.getAsLong()))
            .registerTypeAdapter(Duration.class, (com.google.gson.JsonSerializer<Duration>) (src, type, context) -> context.serialize(src.toMinutes()))
            .create();

    public TasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void handleRequest(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if ("GET".equals(method) && "/tasks".equals(path)) {
            handleGetAllTasks(exchange);
        } else if ("POST".equals(method) && "/tasks".equals(path)) {
            handlePostTask(exchange);
        } else if ("GET".equals(method) && path.matches("/tasks/\\d+")) {
            handleGetTaskById(exchange);
        } else if ("DELETE".equals(method) && path.matches("/tasks/\\d+")) {
            handleDeleteTaskById(exchange);
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }

    private void handleGetAllTasks(HttpExchange exchange) throws IOException {
        String response = gson.toJson(taskManager.getAllTasks());
        sendResponse(exchange, response, 200);
    }

    private void handlePostTask(HttpExchange exchange) throws IOException {
        String body = extractRequestBody(exchange);
        Task task = gson.fromJson(body, Task.class);
        if (task.getId() == 0) {
            taskManager.createTask(task);
            sendResponse(exchange, "Задача создана", 201);
        } else {
            taskManager.updateTask(task);
            sendResponse(exchange, "Задача обновлена", 200);
        }
    }

    private void handleGetTaskById(HttpExchange exchange) throws IOException {
        int id = extractId(exchange.getRequestURI().getPath());
        Task task = Optional.ofNullable(taskManager.getTaskById(id)).orElseThrow(() -> new NotFoundException("Задача не найдена"));
        String response = gson.toJson(task);
        sendResponse(exchange, response, 200);
    }

    private void handleDeleteTaskById(HttpExchange exchange) throws IOException {
        int id = extractId(exchange.getRequestURI().getPath());
        taskManager.deleteTask(id);
        sendResponse(exchange, "Задача удалена", 200);
    }
}
