package ru.yandex.practicum.filmorate;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.Check;

import java.util.Random;

@SpringBootTest
public class FilmControllerTest {
    private FilmController filmController;
    private Faker faker;
    private Random random;

    @BeforeEach
    public void setUp() {
        filmController = new FilmController();
        faker = new Faker();
        random = new Random();
    }

    @Test
    public void create() {
        Film film = Film.builder()
                .name(faker.book().title())
                .description(faker.book().title())
                .releaseDate(Check.movieBirthday.plusDays(1))
                .duration(random.nextLong(1000) + 1)
                .build();
        Assertions.assertTrue(filmController.findAll().isEmpty());
        Film filmCrete = filmController.create(film);
        Assertions.assertTrue(filmController.findAll().contains(filmCrete));
    }

    @Test
    public void createInvalidName() {
        Film film = Film.builder()
                .name(null)
                .description(faker.book().title())
                .releaseDate(Check.movieBirthday)
                .duration(random.nextLong(1000) + 1)
                .build();
        Assertions.assertTrue(filmController.findAll().isEmpty());
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> filmController.create(film));
        Assertions.assertEquals("значение name не должно быть пустым", validationException.getMessage());
        Assertions.assertTrue(filmController.findAll().isEmpty());
        film.setName(" ");
        validationException = Assertions.assertThrows(ValidationException.class,
                () -> filmController.create(film));
        Assertions.assertEquals("значение name не должно быть пустым", validationException.getMessage());
        Assertions.assertTrue(filmController.findAll().isEmpty());
    }

    @Test
    public void createInvalidDescription() {
        Film film = Film.builder()
                .name(faker.book().title())
                .description(RandomStringUtils.random(201))
                .releaseDate(Check.movieBirthday.plusDays(1))
                .duration(random.nextLong(1000) + 1)
                .build();
        Assertions.assertTrue(filmController.findAll().isEmpty());
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> filmController.create(film));
        Assertions.assertEquals("описание не должно превышать 200 символов", validationException.getMessage());
        Assertions.assertTrue(filmController.findAll().isEmpty());
    }

    @Test
    public void createInvalidDuration() {
        Film film = Film.builder()
                .name(faker.book().title())
                .description(faker.book().title())
                .releaseDate(Check.movieBirthday.plusDays(1))
                .duration(-1L)
                .build();
        Assertions.assertTrue(filmController.findAll().isEmpty());
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> filmController.create(film));
        Assertions.assertEquals("продолжительность не может быть отрицательным числом", validationException.getMessage());
        Assertions.assertTrue(filmController.findAll().isEmpty());
    }

    @Test
    public void createInvalidReleaseDate() {
        Film film = Film.builder()
                .name(faker.book().title())
                .description(faker.book().title())
                .releaseDate(Check.movieBirthday.minusDays(1))
                .duration(random.nextLong(1000) + 1)
                .build();
        Assertions.assertTrue(filmController.findAll().isEmpty());
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> filmController.create(film));
        Assertions.assertEquals("дата не может быть реньше чем 1895-12-28", validationException.getMessage());
        Assertions.assertTrue(filmController.findAll().isEmpty());
    }

    @Test
    public void update() {
        Film film = Film.builder()
                .name(faker.book().title())
                .description(faker.book().title())
                .releaseDate(Check.movieBirthday.plusDays(1))
                .duration(random.nextLong(1000) + 1)
                .build();
        Film updateFilm = Film.builder()
                .name(faker.book().title())
                .description(faker.book().title())
                .releaseDate(Check.movieBirthday.plusDays(2))
                .duration(random.nextLong(1000) + 1)
                .build();
        Assertions.assertTrue(filmController.findAll().isEmpty());
        Film filmCrete = filmController.create(film);
        Assertions.assertTrue(filmController.findAll().contains(filmCrete));
        Film filmUpdate = filmController.update(updateFilm, film.getId());
        Assertions.assertEquals(1, filmController.findAll().size());
        Assertions.assertTrue(filmController.findAll().contains(filmUpdate));
        Assertions.assertFalse(filmController.findAll().contains(film));
    }

    @Test
    public void updateInvalidId() {
        Film film = Film.builder()
                .name(faker.book().title())
                .description(faker.book().title())
                .releaseDate(Check.movieBirthday.plusDays(1))
                .duration(random.nextLong(1000) + 1)
                .build();
        Assertions.assertTrue(filmController.findAll().isEmpty());
        long randomId = random.nextLong(100);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> filmController.update(film, randomId));
        Assertions.assertEquals("фильма с id [ " + randomId + " ] не существует", validationException.getMessage());
        Assertions.assertTrue(filmController.findAll().isEmpty());
    }
}
