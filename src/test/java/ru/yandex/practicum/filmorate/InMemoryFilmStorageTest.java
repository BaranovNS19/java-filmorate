package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InMemoryFilmStorageTest {
    private Faker faker;
    private Random random;
    private HttpClient httpClient;
    private ObjectMapper objectMapper;
    private String baseUrl;
    private InMemoryFilmStorage inMemoryFilmStorage;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
        random = new Random();
        httpClient = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        baseUrl = "http://localhost:" + port + "/films";
        inMemoryFilmStorage = new InMemoryFilmStorage();
    }

    @Test
    public void create() throws IOException, InterruptedException {
        Film film = Film.builder()
                .name(faker.book().title())
                .description(faker.book().title())
                .releaseDate(InMemoryFilmStorage.movieBirthday.plusDays(1))
                .duration(random.nextLong(1000) + 1)
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(film)))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void createInvalidName() throws IOException, InterruptedException {
        Film film = Film.builder()
                .name(null)
                .description(faker.book().title())
                .releaseDate(InMemoryFilmStorage.movieBirthday)
                .duration(random.nextLong(1000) + 1)
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(film)))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(400, response.statusCode());
    }

    @Test
    public void createInvalidDescription() throws IOException, InterruptedException {
        Film film = Film.builder()
                .name(faker.book().title())
                .description(RandomStringUtils.random(201))
                .releaseDate(InMemoryFilmStorage.movieBirthday.plusDays(1))
                .duration(random.nextLong(1000) + 1)
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(film)))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(400, response.statusCode());
    }

    @Test
    public void createInvalidDuration() throws IOException, InterruptedException {
        Film film = Film.builder()
                .name(faker.book().title())
                .description(faker.book().title())
                .releaseDate(InMemoryFilmStorage.movieBirthday.plusDays(1))
                .duration(-1L)
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(film)))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(400, response.statusCode());
    }

    @Test
    public void createInvalidReleaseDate() throws IOException, InterruptedException {
        Film film = Film.builder()
                .name(faker.book().title())
                .description(faker.book().title())
                .releaseDate(InMemoryFilmStorage.movieBirthday.minusDays(1))
                .duration(random.nextLong(1000) + 1)
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(film)))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(400, response.statusCode());
    }

    @Test
    public void update() throws IOException, InterruptedException {
        Film film = Film.builder()
                .name(faker.book().title())
                .description(faker.book().title())
                .releaseDate(InMemoryFilmStorage.movieBirthday.plusDays(1))
                .duration(random.nextLong(1000) + 1)
                .build();
        Film updateFilm = Film.builder()
                .id(1)
                .name(faker.book().title())
                .description(faker.book().title())
                .releaseDate(InMemoryFilmStorage.movieBirthday.plusDays(2))
                .duration(random.nextLong(1000) + 1)
                .build();
        Assertions.assertTrue(inMemoryFilmStorage.findAll().isEmpty());
        HttpRequest requestPost = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(film)))
                .build();
        httpClient.send(requestPost, HttpResponse.BodyHandlers.ofString());
        HttpRequest requestPut = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(updateFilm)))
                .build();
        HttpResponse<String> response = httpClient.send(requestPut, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void updateInvalidId() throws IOException, InterruptedException {
        Film film = Film.builder()
                .name(faker.book().title())
                .description(faker.book().title())
                .releaseDate(InMemoryFilmStorage.movieBirthday.plusDays(1))
                .duration(random.nextLong(1000) + 1)
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(film)))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response.statusCode());
    }
}
