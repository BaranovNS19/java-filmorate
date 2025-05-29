package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;

@Repository
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, releaseDate, duration) " +
            "VALUES (?, ?, ?, ?)";
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_FILM_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?";
    private static final String INSERT_FILM_GENRE_QUERY = "INSERT INTO movie_genres(genre_id, film_id) " +
            "VALUES (?, ?)";
    private static final String FIND_GENRE_BY_FILM_QUERY = "SELECT genre_id FROM movie_genres WHERE film_id = ?";
    private static final String INSERT_FILM_RAITING_QUERY = "INSERT INTO movie_raiting(raiting_id, film_id) " +
            "VALUES (?, ?)";
    private static final String FIND_RAITING_BY_FILM_QUERY = "SELECT raiting_id FROM movie_raiting WHERE film_id = ?";
    private static final String UPDATE_FILM_QUERY = "UPDATE films SET name = ?, description = ?, releaseDate = ?, " +
            "duration = ? WHERE id = ?;";
    private static final String UPDATE_RAITING_BY_FILM = "UPDATE movie_raiting SET raiting_id = ? WHERE film_id = ?;";
    private static final String DELETE_GENRES_BY_FILM_QUERY = "DELETE FROM movie_genres WHERE film_id = ?";
    private static final String INSERT_ADD_LIKE = "INSERT INTO likes(user_id, film_id) VALUES (?, ?);";
    private static final String FIND_LIKES_BY_FILM = "SELECT user_id FROM likes WHERE film_id = ?;";
    private static final String DELETE_LIKE_BY_FILM_QUERY = "DELETE FROM likes WHERE film_id = ? AND user_id = ?;";
    private static final String FIND_POPULAR_FILMS_QUERY = "SELECT film_id\n" +
            "FROM (SELECT film_id, COUNT(user_id) AS like_count \n" +
            "FROM likes\n" +
            "GROUP BY film_id\n" +
            "ORDER BY like_count DESC\n" +
            "LIMIT ?)";

    @Autowired
    private MpaDbStorage mpaDbStorage;
    @Autowired
    private GenreDbStorage genreDbStorage;

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Film create(Film film) {
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration()
        );
        film.setLikes(new ArrayList<>());
        film.setId(id);
        List<Genre> genresId = film.getGenres();
        for (Genre genre : genresId) {
            insert(INSERT_FILM_GENRE_QUERY, genre.getId(), film.getId());
        }
        insert(INSERT_FILM_RAITING_QUERY, film.getMpa().getId(), film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        update(UPDATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId()
        );
        update(UPDATE_RAITING_BY_FILM, film.getMpa().getId(), film.getId());
        delete(DELETE_GENRES_BY_FILM_QUERY, film.getId());
        List<Genre> genresId = film.getGenres();
        for (Genre genre : genresId) {
            insert(INSERT_FILM_GENRE_QUERY, genre.getId(), film.getId());
        }
        return getFilmById(film.getId());
    }

    @Override
    public Film getFilmById(long id) {
        return findOne(FIND_FILM_BY_ID_QUERY, id);
    }

    @Override
    public Film addLike(long filmId, long userId) {
        insert(INSERT_ADD_LIKE, userId, filmId);
        return getFilmById(filmId);
    }

    @Override
    public List<Long> getLikesByFilm(long id) {
        return jdbc.queryForList(FIND_LIKES_BY_FILM, Long.class, id);
    }

    @Override
    public boolean deleteLikeByFilm(long filmId, long userId) {
        return delete(DELETE_LIKE_BY_FILM_QUERY, filmId, userId);
    }

    @Override
    public Collection<Long> getPopularFilms(long count) {
        return jdbc.queryForList(FIND_POPULAR_FILMS_QUERY, Long.class, count);
    }

    public List<Genre> getGenresByFilm(long id) {
        List<Genre> genresByFilm = new ArrayList<>();
        List<Long> genreId = jdbc.queryForList(FIND_GENRE_BY_FILM_QUERY, Long.class, id);
        for (Long idg : genreId) {
            genresByFilm.add(genreDbStorage.getGenreById(idg));
        }
        return genresByFilm;

    }

    public Mpa getRaitingByFilm(long id) {
        Long raitingId = jdbc.queryForObject(FIND_RAITING_BY_FILM_QUERY, Long.class, id);
        if (raitingId != null) {
            return mpaDbStorage.getRaitingById(raitingId);
        }
        return new Mpa(null, null);
    }
}


