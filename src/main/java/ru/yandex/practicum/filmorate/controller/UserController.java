package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.Check;

import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final HashMap<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
       /* if (!Check.checkLogin(user.getLogin())) {
            log.error("некорректный формат логина");
            throw new ValidationException("некорректный формат логина");
        }*/
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        /*if (!Check.checkOfBirth(user.getBirthday())) {
            log.error("некорректно указана дата рождения");
            throw new ValidationException("некорректно указана дата рождения");
        }*/
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("добавлен пользователь {}", user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
       /* if (!users.containsKey(id)) {
            log.error("пользователя с id [ {} ] не существует", id);
            throw new ValidationException("пользователя с id [ " + id + " ] не существует");
        }
        if (!Check.checkLogin(user.getLogin())) {
            log.error("некорректный формат логина ");
            throw new ValidationException("некорректный формат логина");
        }*/
        if (!users.containsKey(user.getId())){
            log.error("пользователя с id [ {} ] не существует", user.getId());
            throw new ValidationException("пользователя с id [ " + user.getId() + " ] не существует");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
       /* if (!Check.checkOfBirth(user.getBirthday())) {
            log.error("некорректно указана дата рождения ");
            throw new ValidationException("некорректно указана дата рождения");
        }*/
       // user.setId(id);
        users.put(user.getId(), user);
        log.info("данные пользователя {} обновлены", user);
        return user;
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
