package ru.yandex.practicum.filmorate;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.FriendRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;

import java.time.LocalDate;
import java.util.ArrayList;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserRowMapper.class, FriendsDbStorage.class, FriendRowMapper.class})
class UserStorageTests {
    private final UserDbStorage userStorage;

    @Test
    public void testFindUserById() {
        Faker faker = new Faker();
        User user = User.builder()
                .email(faker.internet().emailAddress())
                .login(faker.name().username())
                .name(String.valueOf(faker.name()))
                .birthday(LocalDate.ofEpochDay(faker.number().numberBetween(1900, LocalDate.now().getYear())))
                .build();
        userStorage.create(user);

        User userDb = userStorage.getUserById(user.getId());
        Assertions.assertEquals(userDb.getLogin(), user.getLogin());
        Assertions.assertEquals(userDb.getEmail(), user.getEmail());
        Assertions.assertEquals(userDb.getName(), user.getName());
        Assertions.assertEquals(userDb.getBirthday(), user.getBirthday());
    }

    @Test
    public void testUserCrete(){
        Faker faker = new Faker();
        User user = User.builder()
                .email(faker.internet().emailAddress())
                .login(faker.name().username())
                .name(String.valueOf(faker.name()))
                .birthday(LocalDate.ofEpochDay(faker.number().numberBetween(1900, LocalDate.now().getYear())))
                .build();
        userStorage.create(user);
        Assertions.assertEquals(userStorage.getUserById(user.getId()), user);
    }

    @Test
    public void testUserUpdate(){
        Faker faker = new Faker();
        User user = User.builder()
                .email(faker.internet().emailAddress())
                .login(faker.name().username())
                .name(String.valueOf(faker.name()))
                .birthday(LocalDate.ofEpochDay(faker.number().numberBetween(1900, LocalDate.now().getYear())))
                .build();
        userStorage.create(user);
        User userUpdate = User.builder()
                .email(faker.internet().emailAddress())
                .login(faker.name().username())
                .name(String.valueOf(faker.name()))
                .birthday(LocalDate.ofEpochDay(faker.number().numberBetween(1900, LocalDate.now().getYear())))
                .build();
        userUpdate.setId(user.getId());
        userUpdate.setFriends(new ArrayList<>());
        Assertions.assertEquals(userStorage.update(userUpdate), userUpdate);
    }
}