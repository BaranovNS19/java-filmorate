package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.AddException;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private InMemoryUserStorage inMemoryUserStorage;

    public User addFriend(Long id, Long friendId) {
        User user = inMemoryUserStorage.getUserById(id);
        User friend = inMemoryUserStorage.getUserById(friendId);
        Set<Friend> userFriends = user.getFriends();
        if (!userFriends.add(new Friend(friendId))) {
            throw new AddException("пользователь [" + friendId + "] уже есть в друзьях");
        }
        friend.getFriends().add(new Friend(id));
        return user;
    }

    public Set<Friend> getFriendsByUser(Long id) {
        return inMemoryUserStorage.getUserById(id).getFriends();
    }

    public User deleteFriend(Long id, Long friendId) {
        Set<Friend> firstUserFriends = inMemoryUserStorage.getUserById(id).getFriends();
        firstUserFriends.removeIf(f -> f.getId() == friendId);
        Set<Friend> secondUserFriend = inMemoryUserStorage.getUserById(friendId).getFriends();
        secondUserFriend.removeIf(f -> f.getId() == id);
        return inMemoryUserStorage.getUserById(id);
    }

    public Set<Friend> getCommonFriendsByUser(Long id, Long otherId) {
        Set<Friend> firstUserFriends = inMemoryUserStorage.getUserById(id).getFriends();
        Set<Friend> secondUserFriends = inMemoryUserStorage.getUserById(otherId).getFriends();
        return firstUserFriends.stream().filter(secondUserFriends::contains).collect(Collectors.toSet());
    }
}
