package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidatorTest {

    @Test
    void createUserTest() {
        User user = new User();
        user.setEmail("johnsmith@gmail.com");
        user.setLogin("John_Smith");
        user.setBirthday(LocalDate.of(2005, 12, 31));

        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    void invalidEmailTest() {
        User user = new User();
        user.setEmail("johnsmith_gmail.com");
        user.setLogin("John_Smith");
        user.setBirthday(LocalDate.of(2005, 12, 31));
        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    void invalidLoginTest() {
        User user = new User();
        user.setLogin("johnsmith@gmail.com");
        user.setLogin("John Smith");
        user.setBirthday(LocalDate.of(2005, 12, 31));
        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    void invalidBirthdayTest() {
        User user = new User();
        user.setLogin("johnsmith@gmail.com");
        user.setLogin("John_Smith");
        user.setBirthday(LocalDate.of(2025, 12, 31));
        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }
}
