package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.request.CreateUserRequest;
import ru.yandex.practicum.filmorate.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static User mapToUser(CreateUserRequest request) {
        User user = new User();
        user.setLogin(request.getLogin());
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setBirthday(request.getBirthday());
        return user;
    }
}
