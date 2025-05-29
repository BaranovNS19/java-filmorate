package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    @Lazy
    @Autowired
    public FilmRowMapper(FilmDbStorage filmDbStorage, UserDbStorage userDbStorage) {
        this.filmDbStorage = filmDbStorage;
        this.userDbStorage = userDbStorage;
    }

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("releaseDate").toLocalDate());
        film.setDuration(resultSet.getLong("duration"));
        film.setGenres(filmDbStorage.getGenresByFilm(resultSet.getLong("id")));
        film.setMpa(filmDbStorage.getRaitingByFilm(resultSet.getLong("id")));
        Mpa mpa = filmDbStorage.getRaitingByFilm(resultSet.getLong("id"));
        film.setMpa(mpa);

        List<Like> likes = new ArrayList<>();
        for (Long idl : filmDbStorage.getLikesByFilm(resultSet.getLong("id"))) {
            likes.add(new Like(userDbStorage.getUserById(idl)));
        }
        film.setLikes(likes);
        return film;
    }
}
