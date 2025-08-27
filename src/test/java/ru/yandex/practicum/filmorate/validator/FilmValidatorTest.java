package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidatorTest {

    @Test
    void createFilmTest() {
        Film film = new Film(null, "Аватар", "Описание фильма",
                LocalDate.of(2009, 12, 18), Duration.of(162, ChronoUnit.MINUTES));
        assertDoesNotThrow(() -> FilmValidator.validate(film));
    }

    @Test
    void invalidFilmNameTest() {
        Film film = new Film(null, null, "Описание фильма",
                LocalDate.of(2009, 12, 18), Duration.of(162, ChronoUnit.MINUTES));
        assertThrows(ValidationException.class, () -> FilmValidator.validate(film));
    }

    @Test
    void invalidFilmReleaseDateTest() {
        Film film = new Film(null, "Аватар", "Описание фильма",
                LocalDate.of(1894, 12, 18), Duration.of(162, ChronoUnit.MINUTES));
        assertThrows(ValidationException.class, () -> FilmValidator.validate(film));
    }

    @Test
    void invalidFilmDurationTest() {
        Film film = new Film(null, "Аватар", "Описание фильма",
                LocalDate.of(2009, 12, 18), Duration.of(-10, ChronoUnit.MINUTES));
        assertThrows(ValidationException.class, () -> FilmValidator.validate(film));
    }
}