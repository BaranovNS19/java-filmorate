package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class GenreDbStorage extends BaseRepository<Genre> {
    private static final String FIND_GENRE_BY_ID_QUERY = "SELECT * FROM genre WHERE genre_id = ?;";
    private static final String FIND_GENRE_ALL_QUERY = "SELECT * FROM genre;";
    private static final String FIND_GENRE_BY_FILM_QUERY = "SELECT genre_id FROM movie_genres WHERE film_id = ?";

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

    public List<Genre> getGenresByFilm(long id) {
        List<Genre> genresByFilm = new ArrayList<>();
        List<Long> genreId = jdbc.queryForList(FIND_GENRE_BY_FILM_QUERY, Long.class, id);
        for (Long idg : genreId) {
            genresByFilm.add(getGenreById(idg));
        }
        return genresByFilm;

    }
}
