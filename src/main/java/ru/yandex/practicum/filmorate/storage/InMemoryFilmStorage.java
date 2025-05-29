package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Deprecated
@Slf4j
@Component
@Getter
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Long, Film> films = new HashMap<>();
    public static final LocalDate movieBirthday = LocalDate.of(1895, 12, 28);

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film create(@Valid Film film) {
        if (film.getReleaseDate().isBefore(movieBirthday)) {
            log.error("дата не может быть реньше чем {}", movieBirthday);
            throw new ValidationException("дата не может быть реньше чем " + movieBirthday);
        }
        film.setId(getNextId());
        film.setLikes(new ArrayList<>());
        films.put(film.getId(), film);
        log.info("добавлен фильм {}", film);
        return film;
    }

    @Override
    public Film update(@Valid Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("фильма с id [ {} ] не существует", film.getId());
            throw new NotFoundException("фильма с id [ " + film.getId() + " ] не существует");
        }
        if (film.getReleaseDate().isBefore(movieBirthday)) {
            log.error("дата не может быть реньше чем {}", movieBirthday);
            throw new ValidationException("дата не может быть реньше чем " + movieBirthday);
        }
        film.setLikes(getFilmById(film.getId()).getLikes());
        films.put(film.getId(), film);
        log.info("обновлен фильм {}", film);
        return film;
    }

    public Film getFilmById(long id) {
        if (!films.containsKey(id)) {
            throw new IncorrectParameterException("фильм с id [" + id + "] не найден");
        }
        return films.get(id);
    }

    @Override
    public Film addLike(long filmId, long userId) {
        return null;
    }

    @Override
    public List<Long> getLikesByFilm(long id) {
        return List.of();
    }

    @Override
    public boolean deleteLikeByFilm(long filmId, long userId) {
        return false;
    }

    @Override
    public Collection<Long> getPopularFilms(long count) {
        return List.of();
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
