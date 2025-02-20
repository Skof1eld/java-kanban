package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.Managers;
import logic.TaskManager;
import model.Epic;
import model.Subtask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public EpicHandler(Gson gson) {
        this.taskManager = Managers.getDefault();
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String[] parts = path.split("/");

            if (method.equals("GET") && parts.length == 4 && parts[1].equals("epics") && parts[3].equals("subtasks")) {
                int epicId = Integer.parseInt(parts[2]);
                handleGetSubtasksByEpicId(exchange, epicId);
                return;
            }

            if (method.equals("POST") && parts.length == 5 && parts[1].equals("epics") && parts[3].equals("tasks")) {
                int epicId = Integer.parseInt(parts[2]);
                int subtaskId = Integer.parseInt(parts[4]);
                addSubtaskToEpic(exchange, epicId, subtaskId);
                return;
            }

            switch (method) {
                case "GET":
                    handleGetEpics(exchange);
                    break;
                case "POST":
                    handlePostEpic(exchange);
                    break;
                case "DELETE":
                    handleDeleteEpic(exchange);
                    break;
                default:
                    sendText(exchange, "Метод не поддерживается", 405);
            }
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        List<Epic> epics = taskManager.getAllEpics();
        String response = gson.toJson(epics);
        sendText(exchange, response, 200);
    }

    private void handlePostEpic(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(body, Epic.class);
        taskManager.addEpic(epic);
        sendText(exchange, "Эпик добавлен", 201);
    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");
        if (parts.length == 3) {
            int id = Integer.parseInt(parts[2]);
            Epic epic = taskManager.getEpic(id);
            if (epic != null) {
                taskManager.removeEpic(id);
                sendText(exchange, "Эпик удален", 200);
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private void addSubtaskToEpic(HttpExchange exchange, int epicId, int subtaskId) throws IOException {
        Epic epic = taskManager.getEpic(epicId);
        Subtask subtask = taskManager.getSubtask(subtaskId);

        if (epic != null && subtask != null) {
            subtask.setEpicId(epicId);
            taskManager.updateSubtask(subtask);
            sendText(exchange, "Подзадача добавлена в эпик", 200);
        } else {
            sendNotFound(exchange);
        }
    }

    private void handleGetSubtasksByEpicId(HttpExchange exchange, int epicId) throws IOException {
        List<Subtask> subtasks = taskManager.getAllSubtasksOfOneEpic(epicId);
        if (subtasks.isEmpty()) {
            sendText(exchange, "Подзадачи не найдены", 404);
        } else {
            String response = gson.toJson(subtasks);
            sendText(exchange, response, 200);
        }
    }
}
