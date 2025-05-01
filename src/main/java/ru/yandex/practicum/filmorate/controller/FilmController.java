package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
@Slf4j()
public class FilmController {

    private final HashMap<Long, Film> films = new HashMap<>();
    public static final LocalDate movieBirthday = LocalDate.of(1895, 12, 28);

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        if (film.getReleaseDate().isBefore(movieBirthday)) {
            log.error("дата не может быть реньше чем {}", movieBirthday);
            throw new ValidationException("дата не может быть реньше чем " + movieBirthday);
        }
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("добавлен фильм {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("фильма с id [ {} ] не существует", film.getId());
            throw new ValidationException("фильма с id [ " + film.getId() + " ] не существует");
        }
        if (film.getReleaseDate().isBefore(movieBirthday)) {
            log.error("дата не может быть реньше чем {}", movieBirthday);
            throw new ValidationException("дата не может быть реньше чем " + movieBirthday);
        }
        films.put(film.getId(), film);
        log.info("обновлен фильм {}", film);
        return film;
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
