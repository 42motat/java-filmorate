package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmService {
    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);

    Collection<Film> getFilmsWithMostLikes(int likesLimit);
}
