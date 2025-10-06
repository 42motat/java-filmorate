package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    // работа с хранилищем
    @GetMapping
    public Collection<FilmDto> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PostMapping
    public FilmDto create(@Valid @RequestBody NewFilmRequest newFilmRequest) {
        log.debug("Фильм успешно добавлен в список фильмов; id={}", newFilmRequest.getId());
        return filmService.createFilm(newFilmRequest);
    }

    @PutMapping
    public FilmDto update(@Valid @RequestBody UpdateFilmRequest updateFilmRequest) {
        log.debug("Фильм с id={} успешно обновлён", updateFilmRequest.getId());
        return filmService.updateFilm(updateFilmRequest);
    }

    @GetMapping("/{filmId}")
    public FilmDto getById(@PathVariable long filmId) {
        log.trace("Поиск фильма по id={}", filmId);
        return filmService.getById(filmId);
    }

    // работа с лайками
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

    @GetMapping("/popular")
    public Collection<FilmDto> getFilmsWithMostLikes(@RequestParam(defaultValue = "10") int count) {
        log.trace("Выводим список из {} первых по популярности фильмов", count);
        return filmService.getFilmsWithMostLikes(count);
    }
}
