package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.Check;

import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
@Slf4j()
public class FilmController {

    private final HashMap<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
       /* if (!Check.fieldIsNotEmpty(film.getName())) {
            log.info("значение name не должно быть пустым");
            throw new ValidationException("значение name не должно быть пустым");
        }
        if (!Check.checkMaxLengthField(film.getDescription(), 200L)) {
            log.error("описание не должно превышать 200 символов");
            throw new ValidationException("описание не должно превышать 200 символов");
        }
        if (!Check.checkDateRelease(film.getReleaseDate())) {
            log.error("дата не может быть реньше чем {}", Check.movieBirthday);
            throw new ValidationException("дата не может быть реньше чем " + Check.movieBirthday);
        }
        if (Check.checkDuration(film.getDuration())) {
            log.error("продолжительность не может быть отрицательным числом");
            throw new ValidationException("продолжительность не может быть отрицательным числом");
        }*/
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("добавлен фильм {}", film);
        return film;
    }

    @PutMapping("{id}")
    public Film update(@RequestBody @Valid Film film, @PathVariable Long id) {
        if (!films.containsKey(id)) {
            log.error("фильма с id [ {} ] не существует", id);
            throw new ValidationException("фильма с id [ " + id + " ] не существует");
        }
        if (!Check.fieldIsNotEmpty(film.getName())) {
            log.info("значение name не должно быть пустым ");
            throw new ValidationException("значение name не должно быть пустым");
        }
        if (!Check.checkMaxLengthField(film.getDescription(), 200L)) {
            log.error("описание не должно превышать 200 символов ");
            throw new ValidationException("описание не должно превышать 200 символов");
        }
        if (!Check.checkDateRelease(film.getReleaseDate())) {
            log.error("дата не может быть реньше  чем {}", Check.movieBirthday);
            throw new ValidationException("дата не может быть реньше чем " + Check.movieBirthday);
        }
        if (Check.checkDuration(film.getDuration())) {
            log.error("продолжительность не может быть отрицательным числом ");
            throw new ValidationException("продолжительность не может быть отрицательным числом");
        }
        film.setId(id);
        films.put(id, film);
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
