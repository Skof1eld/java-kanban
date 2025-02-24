package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import logic.Managers;
import logic.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;
    private final TaskManager taskManager;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public HttpTaskServer() throws IOException {
        this.taskManager = Managers.getDefault();
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        initializeHandlers();
    }

    private void initializeHandlers() {
        server.createContext("/tasks", new TaskHandler(taskManager, gson));
        server.createContext("/subtasks", new SubtaskHandler(taskManager, gson));
        server.createContext("/epics", new EpicHandler(taskManager, gson));
        server.createContext("/history", new HistoryHandler(taskManager, gson));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager, gson));

        // * Для тестирования ошибки, ее имитации
        server.createContext("/error-500", exchange -> new BaseHttpHandler(taskManager).sendInternalError(exchange));
    }

    public void start() {
        server.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту.");
    }

    public void stop() {
        server.stop(0);
        System.out.println("HTTP-сервер остановлен.");
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
    }
}
