package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Status;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {
    private HttpTaskServer server;
    private HttpClient client;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @BeforeEach
    void setUp() throws IOException {
        server = new HttpTaskServer();
        server.start();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void getTasksInitiallyEmpty() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals("[]", response.body());
    }

    @Test
    void createTask() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Description", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        String jsonTask = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    // Проверка на удаление задачи, которой не существует
    @Test
    void deleteTaskNotFound() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/999"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    void updateTask() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Description", Status.IN_PROGRESS, Duration.ofMinutes(30), LocalDateTime.now());
        String jsonTask = gson.toJson(task);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .header("Content-Type", "application/json")
                .build();
        client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        task.setNameOfTheTask("Updated Task");
        String updatedJsonTask = gson.toJson(task);
        HttpRequest putRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(updatedJsonTask))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> putResponse = client.send(putRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, putResponse.statusCode());
    }

    @Test
    void getTaskHistory() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/history"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void createSubtaskInEpic() throws IOException, InterruptedException {
        Task epic = new Task("Test Epic", "Description Epic", Status.NEW, Duration.ofMinutes(2), LocalDateTime.now());
        epic.setTaskId(1);
        String jsonEpic = gson.toJson(epic);

        HttpRequest postEpicRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic))
                .header("Content-Type", "application/json")
                .build();
        client.send(postEpicRequest, HttpResponse.BodyHandlers.ofString());

        Task subtask = new Task("Subtask for Epic", "Description Subtask", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        subtask.setTaskId(1);  // Связываем субтаск с эпиком (ID эпика = 1)
        String jsonSubtask = gson.toJson(subtask);

        HttpRequest postSubtaskRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubtask))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(postSubtaskRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    // Проверка работы эндпоинта для получения подзадач из определенного Epic
    @Test
    void getSubtasksForEpic() throws IOException, InterruptedException {
        int epicId = 1;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/epic/" + epicId + "/subtasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void getPrioritizedTasks() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    // Проверка на ошибку 500
    @Test
    void checkError500() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/error-500"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(500, response.statusCode());
        assertEquals("Внутренняя ошибка сервера.", response.body());
    }
}
