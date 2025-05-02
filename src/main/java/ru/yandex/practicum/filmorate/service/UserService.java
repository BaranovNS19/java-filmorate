package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.AddException;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private InMemoryUserStorage inMemoryUserStorage;

    public User addFriend(Long id, Long friendId) {
        User user = inMemoryUserStorage.getUserById(id);
        User friend = inMemoryUserStorage.getUserById(friendId);
        List<Friend> userFriends = user.getFriends();
        for (Friend f : userFriends) {
            if (f.getId() == friendId) {
                throw new AddException("пользователь [" + friendId + "] уже есть в друзьях");
            }
        }
        userFriends.add(new Friend(friendId));
        friend.getFriends().add(new Friend(id));
        return user;
    }

    public List<Friend> getFriendsByUser(Long id) {
        return inMemoryUserStorage.getUserById(id).getFriends();
    }

    public User deleteFriend(Long id, Long friendId) {
        List<Friend> firstUserFriends = inMemoryUserStorage.getUserById(id).getFriends();
        firstUserFriends.removeIf(f -> f.getId() == friendId);
        List<Friend> secondUserFriend = inMemoryUserStorage.getUserById(friendId).getFriends();
        secondUserFriend.removeIf(f -> f.getId() == id);
        return inMemoryUserStorage.getUserById(id);
    }

    public List<Friend> getCommonFriendsByUser(Long id, Long otherId) {
        List<Friend> firstUserFriends = inMemoryUserStorage.getUserById(id).getFriends();
        List<Friend> secondUserFriends = inMemoryUserStorage.getUserById(otherId).getFriends();
        return firstUserFriends.stream().filter(secondUserFriends::contains).collect(Collectors.toList());
    }
}
