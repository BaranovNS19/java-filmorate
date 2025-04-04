package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    private UserController userController;
    private Faker faker;
    private HttpClient httpClient;
    private ObjectMapper objectMapper;
    private String baseUrl;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        userController = new UserController();
        faker = new Faker();
        httpClient = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        baseUrl = "http://localhost:" + port + "/users";

    }

    @Test
    public void create() throws IOException, InterruptedException {
        User user = User.builder()
                .name(faker.name().fullName())
                .login(faker.name().username())
                .email(faker.internet().emailAddress())
                .birthday(LocalDate.now().minusDays(1))
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(user)))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void creteNameIsEmpty() throws IOException, InterruptedException {
        User user = User.builder()
                .name(null)
                .login(faker.name().username())
                .email(faker.internet().emailAddress())
                .birthday(LocalDate.now().minusDays(1))
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(user)))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

    }

    @Test
    public void createInvalidBirthday() throws IOException, InterruptedException {
        User user = User.builder()
                .name(faker.name().fullName())
                .login(faker.name().username())
                .email(faker.internet().emailAddress())
                .birthday(LocalDate.now().plusDays(1))
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(user)))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(400, response.statusCode());
    }

    @Test
    public void createInvalidLogin() throws IOException, InterruptedException {
        User user = User.builder()
                .name(faker.name().fullName())
                .login(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .birthday(LocalDate.now().minusDays(1))
                .build();
        Assertions.assertTrue(userController.findAll().isEmpty());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(user)))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(400, response.statusCode());
    }

    @Test
    public void update() throws IOException, InterruptedException {
        User user = User.builder()
                .name(faker.name().fullName())
                .login(faker.name().username())
                .email(faker.internet().emailAddress())
                .birthday(LocalDate.now().minusDays(1))
                .build();

        User userUpdate = User.builder()
                .id(1)
                .name(faker.name().fullName())
                .login(faker.name().username())
                .email(faker.internet().emailAddress())
                .birthday(LocalDate.now().minusDays(2))
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(user)))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        HttpRequest requestPut = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(userUpdate)))
                .build();
        HttpResponse<String> response = httpClient.send(requestPut, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

    }

    @Test
    public void updateInvalidId() throws IOException, InterruptedException {
        User user = User.builder()
                .name(faker.name().fullName())
                .login(faker.name().username())
                .email(faker.internet().emailAddress())
                .birthday(LocalDate.now().minusDays(1))
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(user)))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(500, response.statusCode());

    }
}
