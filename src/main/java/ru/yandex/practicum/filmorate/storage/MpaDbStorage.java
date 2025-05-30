package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Repository
public class MpaDbStorage extends BaseRepository<Mpa> {
    private static final String FIND_ALL_RAITINGS = "SELECT * FROM raiting;";
    private static final String FIND_RAITING_BY_ID = "SELECT * FROM raiting WHERE id = ?;";
    private static final String FIND_RAITING_BY_FILM_QUERY = "SELECT raiting_id FROM movie_raiting WHERE film_id = ?";

    public MpaDbStorage(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Mpa> getAllRaitings() {
        return findMany(FIND_ALL_RAITINGS);
    }

    public Mpa getRaitingById(long id) {
        return findOne(FIND_RAITING_BY_ID, id);
    }

    public Mpa getMpaByFilm(long id) {
        Long raitingId = jdbc.queryForObject(FIND_RAITING_BY_FILM_QUERY, Long.class, id);
        if (raitingId != null) {
            return getRaitingById(raitingId);
        }
        return new Mpa(null, null);
    }
}
