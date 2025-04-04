package ru.yandex.practicum.filmorate;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Random;

@SpringBootTest
public class UserControllerTest {
    private UserController userController;
    private Faker faker;
    private Random random;

    @BeforeEach
    public void setUp() {
        userController = new UserController();
        faker = new Faker();
        random = new Random();
    }

    @Test
    public void create() {
        User user = User.builder()
                .name(faker.name().fullName())
                .login(faker.name().username())
                .email(faker.internet().emailAddress())
                .birthday(LocalDate.now().minusDays(1))
                .build();
        Assertions.assertTrue(userController.findAll().isEmpty());
        User userCreate = userController.create(user);
        Assertions.assertTrue(userController.findAll().contains(userCreate));
    }

    @Test
    public void creteNameIsEmpty() {
        User user = User.builder()
                .name(null)
                .login(faker.name().username())
                .email(faker.internet().emailAddress())
                .birthday(LocalDate.now().minusDays(1))
                .build();
        Assertions.assertTrue(userController.findAll().isEmpty());
        User userCreate = userController.create(user);
        Assertions.assertTrue(userController.findAll().contains(userCreate));
        Assertions.assertEquals(userCreate.getLogin(), userCreate.getName());
    }

   /* @Test
    public void createInvalidBirthday() {
        User user = User.builder()
                .name(faker.name().fullName())
                .login(faker.name().username())
                .email(faker.internet().emailAddress())
                .birthday(LocalDate.now().plusDays(1))
                .build();
        Assertions.assertTrue(userController.findAll().isEmpty());
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> userController.create(user));
        Assertions.assertEquals("некорректно указана дата рождения", validationException.getMessage());
        Assertions.assertTrue(userController.findAll().isEmpty());
    }

    @Test
    public void createInvalidLogin() {
        User user = User.builder()
                .name(faker.name().fullName())
                .login(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .birthday(LocalDate.now().minusDays(1))
                .build();
        Assertions.assertTrue(userController.findAll().isEmpty());
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> userController.create(user));
        Assertions.assertEquals("некорректный формат логина", validationException.getMessage());
        Assertions.assertTrue(userController.findAll().isEmpty());
        user.setLogin(" ");
        validationException = Assertions.assertThrows(ValidationException.class,
                () -> userController.create(user));
        Assertions.assertEquals("некорректный формат логина", validationException.getMessage());
        user.setLogin(null);
        validationException = Assertions.assertThrows(ValidationException.class,
                () -> userController.create(user));
        Assertions.assertEquals("некорректный формат логина", validationException.getMessage());
    }*/

    /*@Test
    public void update() {
        User user = User.builder()
                .name(faker.name().fullName())
                .login(faker.name().username())
                .email(faker.internet().emailAddress())
                .birthday(LocalDate.now().minusDays(1))
                .build();

        User userUpdate = User.builder()
                .name(faker.name().fullName())
                .login(faker.name().username())
                .email(faker.internet().emailAddress())
                .birthday(LocalDate.now().minusDays(2))
                .build();
        Assertions.assertTrue(userController.findAll().isEmpty());
        User userCreate = userController.create(user);
        Assertions.assertTrue(userController.findAll().contains(userCreate));
        User updateUser = userController.update(userUpdate);
        Assertions.assertEquals(1, userController.findAll().size());
        Assertions.assertTrue(userController.findAll().contains(updateUser));
        Assertions.assertFalse(userController.findAll().contains(user));
    }

    @Test
    public void updateInvalidId() {
        User user = User.builder()
                .name(faker.name().fullName())
                .login(faker.name().username())
                .email(faker.internet().emailAddress())
                .birthday(LocalDate.now().minusDays(1))
                .build();
        Assertions.assertTrue(userController.findAll().isEmpty());
        long randomId = random.nextLong(100);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> userController.update(user));
        Assertions.assertEquals("пользователя с id [ " + randomId + " ] не существует", validationException.getMessage());
        Assertions.assertTrue(userController.findAll().isEmpty());

    }*/
}
