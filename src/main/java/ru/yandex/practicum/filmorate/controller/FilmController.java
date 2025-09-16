/*
* Александру Ф.
* Добрый день!
* Заранее благодарю за код-ревью.
* */

package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.ErrorResponse;
import ru.yandex.practicum.filmorate.exception.AbstractDtoException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    // работа с хранилищем
    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        FilmValidator.validate(film);
        log.debug("Фильм успешно добавлен в список фильмов; id={}", film.getId());
        return filmStorage.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        FilmValidator.validate(film);
        log.debug("Фильм с id={} успешно обновлён", film.getId());
        return filmStorage.update(film);
    }

    @GetMapping("/{filmId}")
    public Optional<Film> getById(@PathVariable long filmId) {
        log.trace("Поиск фильма по id={}", filmId);
        return filmStorage.getById(filmId);
    }

    @DeleteMapping("/{filmId}")
    public Film deleteById(@PathVariable long filmId) {
        log.trace("Удаление фильма с id={}", filmId);
        return filmStorage.deleteById(filmId);
    }

    // работа с лайками
    @GetMapping("/popular")
    public Collection<Film> getFilmsWithMostLikes(@RequestParam(defaultValue = "10") int count) {
        log.trace("Выводим список из {} первых по популярности фильмов", count);
        return filmService.getFilmsWithMostLikes(count);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable long filmId, @PathVariable long userId) {
        log.trace("Добавление лайка фильму id={} от пользователя id={}", filmId, userId);
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable long filmId, @PathVariable long userId) {
        log.trace("Удаление лайка для фильма id={} от пользователя id={}", filmId, userId);
        filmService.removeLike(filmId, userId);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(AbstractDtoException e) {
        return e.toResponse();
    }
}
