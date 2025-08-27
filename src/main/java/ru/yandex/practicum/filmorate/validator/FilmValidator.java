package ru.yandex.practicum.filmorate.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmValidator {

    private static final Logger log = LoggerFactory.getLogger(FilmValidator.class);

    public static void validate(Film film) {
        nameValidation(film.getName());
        descValidation(film.getDescription());
        releaseDateValidation(film.getReleaseDate());
        durationValidation(film.getDuration());
    }

    private static void nameValidation(String name) {
        if (name == null || name.isBlank()) {
            log.error("Ошибка валидации имени");
            throw new ValidationException("Имя не может быть пустым");
        }
    }

    private static void descValidation(String desc) {
        if (desc == null || desc.length() > 200) {
            log.error("Ошибка валидации описания");
            throw new ValidationException("Описание не может превышать 200 символов");
        }
    }

    private static void releaseDateValidation(LocalDate releaseDate) {
        if (releaseDate == null || releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Ошибка валидации даты релиза");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
    }

    private static void durationValidation(long duration) {
        if (duration < 0 || duration > 600) {
            log.error("Ошибка валидации продолжительности");
            throw new ValidationException("Продолжительность фильма не может быть отрицательной " +
                    "и не может быть более 10 часов");
        }
    }
}
