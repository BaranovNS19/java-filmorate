package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    Collection<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    Film getFilmById(long id);

    Film addLike(long filmId, long userId);

    List<Long> getLikesByFilm(long id);

    boolean deleteLikeByFilm(long filmId, long userId);

    Collection<Long> getPopularFilms(long count);
}
