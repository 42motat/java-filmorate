package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {

    public static Film mapToFilm(NewFilmRequest newFilmRequest) {
        Film film = new Film();
        film.setName(newFilmRequest.getName());
        film.setDescription(newFilmRequest.getDescription());
        film.setReleaseDate(newFilmRequest.getReleaseDate());
        film.setDuration(newFilmRequest.getDuration());
        return film;
    }

    public static FilmDto mapToFilmDto(Film film) {
        FilmDto filmDto = new FilmDto();
        filmDto.setId(film.getId());
        filmDto.setName(film.getName());
        filmDto.setDescription(film.getDescription());
        filmDto.setReleaseDate(film.getReleaseDate());
        filmDto.setDuration(film.getDuration());
        filmDto.setGenres(film.getGenres());
        filmDto.setMpa(film.getMpaRating());
        return filmDto;
    }

    public static Film updateFilmFields(Film film, UpdateFilmRequest updateFilmRequest) {
        if (updateFilmRequest.hasName()) {
            film.setName(updateFilmRequest.getName());
        }
        if (updateFilmRequest.hasDesc()) {
            film.setDescription(updateFilmRequest.getDescription());
        }
        if (updateFilmRequest.hasReleaseDate()) {
            film.setReleaseDate(updateFilmRequest.getReleaseDate());
        }
        if (updateFilmRequest.hasDuration()) {
            film.setDuration(updateFilmRequest.getDuration());
        }
        return film;
    }
}
