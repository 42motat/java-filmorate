package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidatorTest {

    @Test
    void createFilmTest() {
        Film film = new Film();
        film.setName("Аватар");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(2009, 12, 18));
        film.setDuration(162);
        assertDoesNotThrow(() -> FilmValidator.validate(film));
    }

    @Test
    void invalidFilmNameTest() {
        Film film = new Film();
        //        film.setName(null);
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(2009, 12, 18));
        film.setDuration(162);
        assertThrows(ValidationException.class, () -> FilmValidator.validate(film));
    }

    @Test
    void invalidFilmReleaseDateTest() {
        Film film = new Film();
        film.setName("Аватар");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(1894, 12, 18));
        film.setDuration(162);
        assertThrows(ValidationException.class, () -> FilmValidator.validate(film));
    }

    @Test
    void invalidFilmDurationTest() {
        Film film = new Film();
        film.setName("Аватар");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(2009, 12, 18));
        film.setDuration(-10);
        assertThrows(ValidationException.class, () -> FilmValidator.validate(film));
    }
}