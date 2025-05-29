package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class GenreDbStorage extends BaseRepository<Genre> {
    private static final String FIND_GENRE_BY_ID_QUERY = "SELECT * FROM genre WHERE genre_id = ?;";
    private static final String FIND_GENRE_ALL_QUERY = "SELECT * FROM genre;";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public Genre getGenreById(long id) {
        return findOne(FIND_GENRE_BY_ID_QUERY, id);
    }

    public Set<Genre> getAllGenres() {
        List<Genre> genresList = findMany(FIND_GENRE_ALL_QUERY);
        return new HashSet<>(genresList);
    }
}
