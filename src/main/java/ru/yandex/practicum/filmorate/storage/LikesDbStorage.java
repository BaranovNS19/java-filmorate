package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;

@Repository
public class LikesDbStorage extends BaseRepository<Like> {
    private static final String INSERT_ADD_LIKE = "INSERT INTO likes(user_id, film_id) VALUES (?, ?);";
    private static final String FIND_LIKES_BY_FILM = "SELECT user_id FROM likes WHERE film_id = ?;";
    private static final String DELETE_LIKE_BY_FILM_QUERY = "DELETE FROM likes WHERE film_id = ? AND user_id = ?;";

    public LikesDbStorage(JdbcTemplate jdbc, RowMapper<Like> mapper) {
        super(jdbc, mapper);
    }

    public void addLike(long filmId, long userId) {
        insert(INSERT_ADD_LIKE, userId, filmId);
    }

    public List<Long> getLikesByFilm(long id) {
        return jdbc.queryForList(FIND_LIKES_BY_FILM, Long.class, id);
    }

    public boolean deleteLikeByFilm(long filmId, long userId) {
        return delete(DELETE_LIKE_BY_FILM_QUERY, filmId, userId);
    }
}
