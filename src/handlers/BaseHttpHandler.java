package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.ManagerSaveException;
import exceptions.NotFoundException;
import manager.TaskManager;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {
    protected static final Gson gson = new Gson();
    protected final TaskManager taskManager;

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            handleRequest(exchange);
        } catch (NotFoundException e) {
            sendNotFound(exchange, e.getMessage());
        } catch (ManagerSaveException e) {
            sendHasInteractions(exchange, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, "Internal Server Error", 500);
        }
    }

    protected abstract void handleRequest(HttpExchange exchange) throws IOException;

    protected void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        System.out.println("длина ответа в байтах = " + responseBytes.length);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream rawOut = exchange.getResponseBody();
             BufferedOutputStream out = new BufferedOutputStream(rawOut)) {
            out.write(responseBytes);
            out.flush();
        } catch (IOException e) {
            System.err.println("Ошибка при отправке данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    protected void sendNotFound(HttpExchange exchange, String message) throws IOException {
        sendResponse(exchange, "Not Found: " + message, 404);
    }

    protected void sendHasInteractions(HttpExchange exchange, String message) throws IOException {
        sendResponse(exchange, "Конфликт: " + message, 409);
    }

    protected String extractRequestBody(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    protected int extractId(String path) {
        String[] segments = path.split("/");
        return Integer.parseInt(segments[segments.length - 1]);
    }
}
