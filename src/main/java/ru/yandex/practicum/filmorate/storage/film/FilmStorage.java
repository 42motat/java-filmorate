package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Film create(Film film);

    Film update(Film film);

    Optional<Film> getById(long filmId);

    void deleteById(long filmId);

    void addLike(long userId, long filmId);

    void removeLike(long userId, long filmId);

    Collection<Film> getFilmsWithMostLikes(int count);
}
