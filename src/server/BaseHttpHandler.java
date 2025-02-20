package server;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
    protected void sendText(HttpExchange exchange, String text, int statusCode) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(statusCode, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        String response = "Ресурс не найден.";
        sendText(exchange, response, 404);
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        String response = "Задача пересекается с существующими.";
        sendText(exchange, response, 406);
    }

    protected void sendInternalError(HttpExchange exchange) throws IOException {
        String response = "Внутренняя ошибка сервера.";
        sendText(exchange, response, 500);
    }
}
