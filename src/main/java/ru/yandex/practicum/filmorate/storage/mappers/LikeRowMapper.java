package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LikeRowMapper implements RowMapper<Like> {
    private final UserDbStorage userDbStorage;

    @Autowired
    public LikeRowMapper(UserDbStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }

    @Override
    public Like mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Like like = new Like();
        like.setUser(userDbStorage.getUserById(resultSet.getLong("user_id")));
        return like;
    }
}
