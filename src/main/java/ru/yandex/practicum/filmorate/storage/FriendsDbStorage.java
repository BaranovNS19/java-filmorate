package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friend;

import java.util.List;

@Repository
public class FriendsDbStorage extends BaseRepository<Friend> {
    private static final String FIND_FRIENDS_BY_USER_QUERY = "SELECT * FROM friends WHERE(user_id = ? AND confirmation_of_friendship = true)" +
            " OR (user_id = ? AND id_requester = ? AND confirmation_of_friendship = false)";
    private static final String FIND_FRIEND_BY_ID_QUERY = "SELECT * FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String UPDATE_CONFIRMATION_FRIENDSHIP = "UPDATE friends SET confirmation_of_friendship = true " +
            "WHERE user_id = ? AND friend_id = ?;";
    private static final String INSERT_QUERY = "INSERT INTO friends(friend_id, user_id, " +
            "id_requester, confirmation_of_friendship)" +
            "VALUES (?, ?, ?, ?)";
    //    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friends WHERE (user_id = ? AND friend_id = ?) OR " +
//            "(user_id = ? AND friend_id = ?);";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String FIND_COMMON_FRIENDS = "SELECT f1.FRIEND_ID AS id, " +
            "f1.user_id, f1.friend_id, f1.id_requester, f1.confirmation_of_friendship " +
            "FROM friends f1 " +
            "JOIN friends f2 ON f1.FRIEND_ID = f2.FRIEND_ID " +
            "WHERE f1.USER_ID = ? " +
            "AND f2.USER_ID = ? " +
            "AND f1.FRIEND_ID NOT IN (?, ?)";

    public FriendsDbStorage(JdbcTemplate jdbc, RowMapper<Friend> mapper) {
        super(jdbc, mapper);
    }

    public List<Friend> getFriendsByUser(long userId) {
        return findMany(FIND_FRIENDS_BY_USER_QUERY, userId, userId, userId);
    }

    public Friend getFriendById(long userId, long friendId) {
        return findOne(FIND_FRIEND_BY_ID_QUERY, userId, friendId);
    }

    public void confirmFriendShip(long userId, long friendId) {
        update(UPDATE_CONFIRMATION_FRIENDSHIP, userId, friendId);
        update(UPDATE_CONFIRMATION_FRIENDSHIP, friendId, userId);
    }

    public void addFriend(long userId, long friendId) {
        insert(
                INSERT_QUERY,
                friendId,
                userId,
                userId,
                false
        );
        insert(
                INSERT_QUERY,
                userId,
                friendId,
                userId,
                false
        );
    }

    public boolean deleteFriend(long userId, long friendId) {
        return delete(DELETE_FRIEND_QUERY, userId, friendId);
    }

    public List<Friend> getCommonFriendsByUser(long id, long otherId) {
        return findMany(FIND_COMMON_FRIENDS, id, otherId, id, otherId);
    }
}
