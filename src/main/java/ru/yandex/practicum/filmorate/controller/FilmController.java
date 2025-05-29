package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film like(@PathVariable long id, @PathVariable long userId) {
        return filmService.like(id, userId);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        return filmService.getFilmById(id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLikeByFilm(@PathVariable long id, @PathVariable long userId) {
        return filmService.removeLikeByFilm(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(required = false, defaultValue = "10") long count) {
        return filmService.getPopularFilms(count);
    }
}
