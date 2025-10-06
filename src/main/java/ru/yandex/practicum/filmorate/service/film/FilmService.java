package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;

import java.util.Collection;

public interface FilmService {
    FilmDto createFilm(NewFilmRequest newFilmRequest);

    FilmDto updateFilm(UpdateFilmRequest updateFilmRequest);

    FilmDto getById(long filmId);

    Collection<FilmDto> getAllFilms();

    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);

    Collection<FilmDto> getFilmsWithMostLikes(int likesLimit);
}
