package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.TaskManager;
import model.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private final Gson gson;

    public PrioritizedHandler(TaskManager taskManager, Gson gson) {
        super(taskManager);
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (HttpMethod.fromString(exchange.getRequestMethod()) == HttpMethod.GET) {
                List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
                String response = gson.toJson(prioritizedTasks);
                sendText(exchange, response, 200);
            } else {
                sendText(exchange, "Метод не поддерживается", 405);
            }
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }
}
