package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.request.CreateUserRequest;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public User create(@RequestBody @Valid CreateUserRequest createUserRequest) {
        return userService.creteUser(createUserRequest);
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable long id, @PathVariable long friendId) {
        return userService.addFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<Friend> getFriendsByUser(@PathVariable long id) {
        return userService.getFriendsByUser(id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<Friend> getCommonFriendsByUser(@PathVariable long id, @PathVariable long otherId) {
        return userService.getCommonFriendsByUser(id, otherId);
    }

    @PutMapping("/friendship/{id}/confirm/{friendId}")
    public User confirmFriendship(@PathVariable long id, @PathVariable long friendId) {
        return userService.confirmFriendship(id, friendId);
    }
}
