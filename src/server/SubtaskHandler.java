package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.Managers;
import logic.TaskManager;
import model.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public SubtaskHandler(Gson gson) {
        this.taskManager = Managers.getDefault();
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            switch (method) {
                case "GET":
                    handleGetSubtasks(exchange);
                    break;
                case "POST":
                    handlePostSubtask(exchange);
                    break;
                case "DELETE":
                    handleDeleteSubtask(exchange);
                    break;
                default:
                    sendText(exchange, "Метод не поддерживается", 405);
            }
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        List<Subtask> subtasks = taskManager.getAllSubtasks();
        if (subtasks.isEmpty()) {
            sendText(exchange, "Подзадачи не найдены", 404);
        } else {
            String response = gson.toJson(subtasks);
            sendText(exchange, response, 200);
        }
    }

    private void handlePostSubtask(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Subtask subtask = gson.fromJson(body, Subtask.class);

        if (subtask.getEpicId() == 0) {
            sendText(exchange, "Ошибка: подзадача должна принадлежать эпику", 400);
            return;
        }

        taskManager.addSubtask(subtask);
        sendText(exchange, "Подзадача добавлена", 201);
    }

    private void handleDeleteSubtask(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");
        if (parts.length == 3) {
            int id = Integer.parseInt(parts[2]);
            Subtask subtask = taskManager.getSubtask(id);
            if (subtask != null) {
                taskManager.removeSubtask(id);
                sendText(exchange, "Подзадача удалена", 200);
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }
}
