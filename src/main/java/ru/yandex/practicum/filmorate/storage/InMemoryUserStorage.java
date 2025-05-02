package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(@Valid User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        user.setFriends(new ArrayList<>());
        users.put(user.getId(), user);
        log.info("добавлен пользователь {}", user);
        return user;
    }

    @Override
    public User update(@Valid User user) {
        if (!users.containsKey(user.getId())) {
            log.error("пользователя с id [ {} ] не существует", user.getId());
            throw new NotFoundException("пользователя с id [ " + user.getId() + " ] не существует");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setFriends(new ArrayList<>());
        users.put(user.getId(), user);
        log.info("данные пользователя {} обновлены", user);
        return user;
    }

    public User getUserById(Long id) {
        if (!users.containsKey(id)) {
            throw new IncorrectParameterException("Пользователь с id " + id + " не найден");
        }
        return users.get(id);
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
