package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    private final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        FilmValidator.validate(film);
        film.setId(getNextId());

        films.put(film.getId(), film);

        log.debug("Фильм успешно добавлен в список фильмов; id={}", film.getId());

        return film;
    }

    public long getNextId() {
        long currentMaxId = films.keySet().stream()
                .mapToLong(filmId -> filmId)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        if (film.getId() == null) {
            throw new FilmNotFoundException("Необходимо указать id фильма," +
                                            " иначе его невозможно будет найти в нашей базе");
        }

        FilmValidator.validate(film);

        if (films.containsKey(film.getId())) {
            Film oldFilm = films.get(film.getId());
            // валидация проходит раньше поиска, поэтому можно считать все переданные значения валидными
            oldFilm.setName(film.getName());
            oldFilm.setDescription(film.getDescription());
            oldFilm.setReleaseDate(film.getReleaseDate());
            oldFilm.setDuration(film.getDuration());

            log.debug("Фильм с id={} успешно обновлён", film.getId());

            return oldFilm;
        }
        throw new FilmNotFoundException("Указанный фильм не найден");
    }

}
