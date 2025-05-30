package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.*;

@Service
public class FilmService {

    @Autowired
    @Qualifier("userDbStorage")
    private UserStorage userStorage;

    @Autowired
    @Qualifier("filmDbStorage")
    private FilmStorage filmStorage;

    @Autowired
    private GenreDbStorage genreDbStorage;
    @Autowired
    private MpaDbStorage raitingDbStorage;
    @Autowired
    private LikesDbStorage likesDbStorage;

    public static final LocalDate movieBirthday = LocalDate.of(1895, 12, 28);

    public Film create(@Valid Film film) {
        if (film.getReleaseDate().isBefore(movieBirthday)) {
            throw new ValidationException("дата не может быть реньше чем " + movieBirthday);
        }
        List<Genre> genresId = film.getGenres();
        if (genresId != null && !genresId.isEmpty()) {
            Set<Genre> uniqueGenres = new LinkedHashSet<>(genresId); // сохраняет порядок
            genresId = new ArrayList<>(uniqueGenres);
            for (Genre g : genresId) {
                if (genreDbStorage.getGenreById(g.getId()) == null) {
                    throw new NotFoundException("Жанр с id [" + g.getId() + "] не найден");
                }
            }
            film.setGenres(genresId);
        } else {
            film.setGenres(new ArrayList<>());
        }
        if (film.getMpa() != null) {
            if (raitingDbStorage.getRaitingById(film.getMpa().getId()) == null) {
                throw new NotFoundException("рейтинг с id [" + film.getMpa() + "] не найден");
            }
        }
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        if (filmStorage.getFilmById(film.getId()) == null) {
            throw new NotFoundException("фильм с id [" + film.getId() + "] не найден");
        }
        List<Genre> genresId = film.getGenres();
        if (genresId != null && !genresId.isEmpty()) {
            for (Genre g : genresId) {
                if (genreDbStorage.getGenreById(g.getId()) == null) {
                    throw new NotFoundException("Жанр с id [" + g.getId() + "] не найден");
                }
            }
        } else {
            film.setGenres(new ArrayList<>());
        }
        if (film.getMpa() != null) {
            if (raitingDbStorage.getRaitingById(film.getMpa().getId()) == null) {
                throw new NotFoundException("рейтинг с id [" + film.getMpa() + "] не найден");
            }
        }

        return filmStorage.update(film);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Set<Genre> getAllGenres() {
        return genreDbStorage.getAllGenres();
    }

    public Genre getGenreById(long id) {
        Genre genre = genreDbStorage.getGenreById(id);
        if (genre == null) {
            throw new IncorrectParameterException("Жанр с id " + id + " не найден");
        }
        return genre;
    }

    public Film getFilmById(long id) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new IncorrectParameterException("фильм с id " + id + " не найден");
        }
        return film;
    }

    public Film like(long id, long userId) {
        User user = userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(id);
        if (user == null) {
            throw new IncorrectParameterException("пользователь с id " + userId + " не найден");
        }
        if (film == null) {
            throw new IncorrectParameterException("фильм с id " + id + " не найден");
        }

        List<Long> likes = likesDbStorage.getLikesByFilm(id);
        if (likes.contains(userId)) {
            throw new AddException("Пользователь с id [" + userId + "] уже ставил лайк");
        }
        likesDbStorage.addLike(id, userId);
        return filmStorage.getFilmById(id);
    }

    public Film removeLikeByFilm(long id, long userId) {
        Film film = filmStorage.getFilmById(id);
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new IncorrectParameterException("пользователь с id " + userId + " не найден");
        }
        if (film == null) {
            throw new IncorrectParameterException("фильм с id " + id + " не найден");
        }

        List<Long> likes = likesDbStorage.getLikesByFilm(id);
        if (!likes.contains(userId)) {
            throw new DeleteException("отсутствует лайк пользователя [" + userId + "]");
        }
        likesDbStorage.deleteLikeByFilm(id, userId);
        return filmStorage.getFilmById(id);
    }

    public Collection<Film> getPopularFilms(Long count) {
        Collection<Film> topFilms = new ArrayList<>();
        for (Long idf : filmStorage.getPopularFilms(count)) {
            topFilms.add(filmStorage.getFilmById(idf));
        }
        return topFilms;
    }

    public Collection<Mpa> getAllMpa() {
        return raitingDbStorage.getAllRaitings();
    }

    public Mpa getMpaById(long id) {
        Mpa raiting = raitingDbStorage.getRaitingById(id);
        if (raiting == null) {
            throw new NotFoundException("рейтинг с [" + id + "] не найден");
        }
        return raiting;
    }
}
