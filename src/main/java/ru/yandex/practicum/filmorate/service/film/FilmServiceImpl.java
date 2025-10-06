package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.GenreDbRepository;
import ru.yandex.practicum.filmorate.repository.MpaRatingDbRepository;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaRatingDbRepository mpaRepository;
    private final GenreDbRepository genreRepository;

    @Override
    public FilmDto createFilm(NewFilmRequest newFilmRequest) {
        List<Genre> requestGenres = new ArrayList<>();
        new HashSet<>(newFilmRequest.getGenres()).forEach(genre -> {
            requestGenres.add(genreRepository.getById(Integer.parseInt(genre.get("id"))).orElseThrow(() ->
                                                      new NotFoundException("Такого жанра нет в базе данных")));
        });

        Film film = FilmMapper.mapToFilm(newFilmRequest);
        film.setGenres(requestGenres);
        film.setMpaRating(mpaRepository.getById(newFilmRequest.getMpa().get("id"))
                                .orElseThrow(() -> new NotFoundException("Такого рейтинга нет в базе данных")));
        film = filmStorage.create(film);
        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public FilmDto updateFilm(UpdateFilmRequest updateFilmRequest) {
        Film updatedFilm = filmStorage.getById(updateFilmRequest.getId())
                .map(film -> FilmMapper.updateFilmFields(film, updateFilmRequest))
                .orElseThrow(() -> new NotFoundException("Такого фильма нет в базе данных"));

        List<Genre> requestGenres = new ArrayList<>();
        new HashSet<>(updateFilmRequest.getGenres()).forEach(genre -> {
            requestGenres.add(genreRepository.getById(Integer.parseInt(genre.get("id"))).orElseThrow(() ->
                    new NotFoundException("Такого жанра нет в базе данных")));
        });
        updatedFilm.setGenres(requestGenres);
        updatedFilm = filmStorage.update(updatedFilm);
        return FilmMapper.mapToFilmDto(updatedFilm);
    }

    @Override
    public FilmDto getById(long filmId) {
        Film film = filmStorage.getById(filmId)
                .orElseThrow(() -> new NotFoundException("Такого фильма нет в базе данных"));
        film.setGenres(genreRepository.getFilmGenres(filmId));
        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public Collection<FilmDto> getAllFilms() {
        Collection<Film> films = filmStorage.getAllFilms();
        return extractFilmListToDto(films);
    }

    // работа с лайками
    @Override
    public void addLike(long filmId, long userId) {
        Film film = filmStorage.getById(filmId)
                .orElseThrow(() -> new NotFoundException("Такого фильма нет в базе данных"));
        User user = userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));

        filmStorage.addLike(userId, filmId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        Film film = filmStorage.getById(filmId)
                .orElseThrow(() -> new NotFoundException("Такого фильма нет в базе данных"));
        User user = userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));

        filmStorage.removeLike(userId, filmId);
    }

    @Override
    public Collection<FilmDto> getFilmsWithMostLikes(int count) {
        Collection<Film> films = filmStorage.getFilmsWithMostLikes(count);
        return films.stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    // "сервисы"
    private Collection<FilmDto> extractFilmListToDto(Collection<Film> films) {
        films.forEach(film -> film.setGenres(genreRepository.getFilmGenres(film.getId())));
        return films
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }
}
