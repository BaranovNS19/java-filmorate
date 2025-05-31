package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<User> {

    private final FriendsDbStorage friendsDbStorage;

    @Autowired
    public UserRowMapper(FriendsDbStorage friendsDbStorage) {
        this.friendsDbStorage = friendsDbStorage;
    }

    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        user.setName(resultSet.getString("name"));
        user.setBirthday(resultSet.getDate("birthday").toLocalDate());
        user.setFriends(friendsDbStorage.getFriendsByUser(resultSet.getLong("id")));
        return user;
    }
}
