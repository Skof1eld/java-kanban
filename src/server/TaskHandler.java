package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.TaskManager;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final Gson gson;

    public TaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager);
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            HttpMethod method = HttpMethod.fromString(exchange.getRequestMethod());
            switch (method) {
                case GET:
                    handleGetTasks(exchange);
                    break;
                case POST:
                    handlePostTask(exchange);
                    break;
                case DELETE:
                    handleDeleteTask(exchange);
                    break;
                default:
                    sendText(exchange, "Метод не поддерживается", 405);
            }
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        List<Task> tasks = taskManager.getAllTasks();
        String response = gson.toJson(tasks);
        sendText(exchange, response, 200);
    }

    private void handlePostTask(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        Task task = gson.fromJson(body, Task.class);
        System.out.println("Парсинг JSON прошел успешно: " + task);

        if (task.getTaskId() == 0) {
            taskManager.addTask(task);
            sendText(exchange, "Задача добавлена", 201);
        } else {
            taskManager.updateTask(task);
            sendText(exchange, "Задача обновлена", 201);
        }
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");
        if (parts.length == 3) {
            int id = Integer.parseInt(parts[2]);
            Task task = taskManager.getTask(id);
            if (task != null) {
                taskManager.removeTask(id);
                sendText(exchange, "Задача удалена", 200);
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }
}
