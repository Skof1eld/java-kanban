package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.Managers;
import logic.TaskManager;
import model.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public HistoryHandler(Gson gson) {
        this.taskManager = Managers.getDefault();
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if ("GET".equals(exchange.getRequestMethod())) {
                List<Task> history = taskManager.getHistory();
                String response = gson.toJson(history);
                sendText(exchange, response, 200);
            } else {
                sendText(exchange, "Метод не поддерживается", 405);
            }
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }
}
