package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.LikesDbStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    private final LikesDbStorage likesDbStorage;
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final UserDbStorage userDbStorage;

    @Autowired
    public FilmRowMapper(LikesDbStorage likesDbStorage, MpaDbStorage mpaDbStorage, GenreDbStorage genreDbStorage,
                         UserDbStorage userDbStorage) {
        this.likesDbStorage = likesDbStorage;
        this.mpaDbStorage = mpaDbStorage;
        this.genreDbStorage = genreDbStorage;
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
        film.setGenres(genreDbStorage.getGenresByFilm(resultSet.getLong("id")));
        film.setMpa(mpaDbStorage.getMpaByFilm(resultSet.getLong("id")));
        List<Like> likes = new ArrayList<>();
        for (Long idl : likesDbStorage.getLikesByFilm(resultSet.getLong("id"))) {
            likes.add(new Like(userDbStorage.getUserById(idl)));
        }
        film.setLikes(likes);

        return film;
    }
}
