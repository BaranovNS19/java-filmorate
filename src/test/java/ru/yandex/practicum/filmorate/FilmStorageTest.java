package ru.yandex.practicum.filmorate;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.MpaRowMappers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, FilmRowMapper.class, MpaDbStorage.class, MpaRowMappers.class, GenreDbStorage.class,
        GenreRowMapper.class})
public class FilmStorageTest {
    private final FilmDbStorage filmDbStorage;

    @Test
    public void testFindFilmById() {
        Faker faker = new Faker();
        Film film = Film.builder()
                .name(String.valueOf(faker.name()))
                .description(faker.book().title())
                .releaseDate(LocalDate.ofEpochDay(faker.number().numberBetween(2000, LocalDate.now().getYear())))
                .duration(faker.number().randomNumber())
                .build();
        film.setGenres(new ArrayList<>());
        film.setMpa(new Mpa(null, null));
        film = filmDbStorage.create(film);
        Film filmDb = filmDbStorage.getFilmById(film.getId());
        Assertions.assertEquals(film.getName(), filmDb.getName());
        Assertions.assertEquals(film.getReleaseDate(), filmDb.getReleaseDate());
        Assertions.assertEquals(film.getDescription(), filmDb.getDescription());
        Assertions.assertEquals(film.getDuration(), filmDb.getDuration());
    }

    @Test
    public void testCreteFilm() {
        Faker faker = new Faker();
        Film film = Film.builder()
                .name(String.valueOf(faker.name()))
                .description(faker.book().title())
                .releaseDate(LocalDate.ofEpochDay(faker.number().numberBetween(2000, LocalDate.now().getYear())))
                .duration(faker.number().randomNumber())
                .build();
        film.setGenres(new ArrayList<>());
        film.setMpa(new Mpa(null, null));
        film = filmDbStorage.create(film);
        Assertions.assertEquals(filmDbStorage.getFilmById(film.getId()), film);
    }

    @Test
    public void testUpdateFilm() {
        Faker faker = new Faker();
        Film film = Film.builder()
                .name(String.valueOf(faker.name()))
                .description(faker.book().title())
                .releaseDate(LocalDate.ofEpochDay(faker.number().numberBetween(2000, LocalDate.now().getYear())))
                .duration(faker.number().randomNumber())
                .build();
        film.setGenres(new ArrayList<>());
        film.setMpa(new Mpa(null, null));
        film = filmDbStorage.create(film);
        Film filmUpdate = Film.builder()
                .name(String.valueOf(faker.name()))
                .description(faker.book().title())
                .releaseDate(LocalDate.ofEpochDay(faker.number().numberBetween(2000, LocalDate.now().getYear())))
                .duration(faker.number().randomNumber())
                .build();
        filmUpdate.setGenres(List.of(new Genre(1, "Комедия")));
        filmUpdate.setMpa(new Mpa(1L, "G"));
        filmUpdate.setId(film.getId());
        filmUpdate.setLikes(new ArrayList<>());
        Assertions.assertEquals(filmDbStorage.update(filmUpdate), filmUpdate);
    }
}
