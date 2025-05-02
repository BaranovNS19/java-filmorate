package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return inMemoryFilmStorage.findAll();
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        return inMemoryFilmStorage.create(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        return inMemoryFilmStorage.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film like(@PathVariable long id, @PathVariable long userId) {
        return filmService.like(id, userId);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        return inMemoryFilmStorage.getFilmById(id);
    }

    @GetMapping("/like/{id}")
    public List<Like> getLikesByFilm(@PathVariable long id) {
        return filmService.getCountLikesByFilm(id);
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
