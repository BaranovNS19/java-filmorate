package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Check {

    public static final LocalDate movieBirthday = LocalDate.of(1895, 12, 28);

    public static boolean fieldIsNotEmpty(String field) {
        return field != null && !field.isBlank();
    }

    public static boolean checkMaxLengthField(String field, Long maxLength) {
        return field.length() <= maxLength;
    }

    public static boolean checkDateRelease(LocalDate date) {
        return date.isAfter(movieBirthday);
    }

    public static boolean checkDuration(long duration) {
        return duration <= 0;
    }

    public static boolean checkLogin(String login) {
        return login != null && !login.isBlank() && !login.contains(" ");
    }

    public static boolean checkOfBirth(LocalDate birth) {
        return birth != null && birth.isBefore(LocalDate.now());
    }
}
