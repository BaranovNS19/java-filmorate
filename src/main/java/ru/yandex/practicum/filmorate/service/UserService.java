package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.request.CreateUserRequest;
import ru.yandex.practicum.filmorate.exeption.AddException;
import ru.yandex.practicum.filmorate.exeption.ConfirmException;
import ru.yandex.practicum.filmorate.exeption.IncorrectParameterException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsDbStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {

    @Autowired
    @Qualifier("userDbStorage")
    private UserStorage userStorage;
    @Autowired
    private FriendsDbStorage friendsDbStorage;

    public User getUserById(Long id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new IncorrectParameterException("Пользователь с id " + id + " не найден");
        }
        return user;
    }

    public User creteUser(@Valid CreateUserRequest createUserRequest) {
        return userStorage.create(UserMapper.mapToUser(createUserRequest));
    }

    public User update(@Valid User user) {
        getUserById(user.getId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.update(user);
    }

    public User addFriend(Long id, Long friendId) {
        if (id == friendId) {
            throw new AddException("добавление в друзья невозможно");
        }
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);

        if (user == null || friend == null) {
            throw new IncorrectParameterException("Пользователь с id " + id + " не найден");
        }

        if (friendsDbStorage.getFriendById(id, friendId) != null) {
            throw new AddException("пользователь [" + friendId + "] уже есть в друзьях");
        }
        friendsDbStorage.addFriend(id, friendId);

        return userStorage.getUserById(id);
    }

    public Collection<Friend> getFriendsByUser(Long id) {
        if (userStorage.getUserById(id) == null) {
            throw new IncorrectParameterException("Пользователь с id " + id + " не найден");
        }
        return friendsDbStorage.getFriendsByUser(id);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User deleteFriend(Long id, Long friendId) {
        if (getUserById(id) == null || getUserById(friendId) == null) {
            throw new IncorrectParameterException("Пользователь с id " + id + " не найден");
        }
        friendsDbStorage.deleteFriend(id, friendId);
        return getUserById(id);
    }

    public List<Friend> getCommonFriendsByUser(Long id, Long otherId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(otherId);
        if (user == null || friend == null) {
            throw new IncorrectParameterException("Пользователь с id " + id + " не найден");
        }
        return friendsDbStorage.getCommonFriendsByUser(id, otherId);
    }

    public User confirmFriendship(Long id, Long friendId) {
        if (id == friendId) {
            throw new ConfirmException("невозможно выполнить операцию");
        }
        User user = userStorage.getUserById(id);
        Friend friend = friendsDbStorage.getFriendById(id, friendId);
        if (friend == null || user == null) {
            throw new ConfirmException("пользователь [" + friendId + "] не совершал запрос на дружбу");
        }
        if (friend.getIdRequester() == id) {
            throw new ConfirmException("пользователь [" + id + "] является инициатором запроса и не имеет " +
                    "возможности подтвердить дружбу");
        }
        if (friend.isConfirmationOfFriendship()) {
            throw new ConfirmException("пользователь [" + id + "] уже подтвердил дружбу ");
        }
        friendsDbStorage.confirmFriendShip(id, friendId);
        return userStorage.getUserById(id);
    }
}
