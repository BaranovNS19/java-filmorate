package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.AddException;
import ru.yandex.practicum.filmorate.exeption.DeleteException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Film like(long id, long userId) {
        User user = inMemoryUserStorage.getUserById(userId);
        Film film = inMemoryFilmStorage.getFilmById(id);
        Set<Like> likes = film.getLikes();
        if (!likes.add(new Like(user))) {
            throw new AddException("Пользователь с id [" + userId + "] уже ставил лайк");
        }
        return film;
    }

    public Set<Like> getCountLikesByFilm(Long id) {
        return inMemoryFilmStorage.getFilmById(id).getLikes();
    }

    public Film removeLikeByFilm(long id, long userId) {
        Film film = inMemoryFilmStorage.getFilmById(id);
        User user = inMemoryUserStorage.getUserById(userId);
        if (user != null) {
            Set<Like> likes = film.getLikes();
            Optional<Like> likeToRemove = likes.stream()
                    .filter(like -> like.getUser().getId() == userId)
                    .findFirst();

            if (likeToRemove.isEmpty()) {
                throw new DeleteException("отсутствует лайк пользователя [" + userId + "]");
            }
            likes.remove(likeToRemove.get());
        }
        return film;
    }

    public Collection<Film> getPopularFilms(Long count) {
        Collection<Film> films = inMemoryFilmStorage.findAll();
        return films.stream()
                .sorted(Comparator.comparingInt(film -> -film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());

    }
}
