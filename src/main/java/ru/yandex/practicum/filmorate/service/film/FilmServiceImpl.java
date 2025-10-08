package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
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
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final MpaRatingDbRepository mpaRepository;
    private final GenreDbRepository genreRepository;
    private final JdbcTemplate jdbc;

    @Override
    public FilmDto createFilm(NewFilmRequest newFilmRequest) {
        List<Genre> requestGenres = genreRepository.getAll().stream().toList();
        List<Genre> givenFilmGenres = new ArrayList<>();
        for (int i = 0; i < newFilmRequest.getGenres().size(); i++) {
            Map<String, String> genreMap = newFilmRequest.getGenres().get(i);
            String genreId = genreMap.get("id");
            if (Integer.parseInt(genreId) > requestGenres.size()) {
                throw new NotFoundException("Такого жанра нет в базе данных");
            } else {
                for (Genre genre : requestGenres) {
                    if (genre.getId().toString().equals(genreId) && !givenFilmGenres.contains(genre)) {
                        givenFilmGenres.add(genre);
                        break;
                    }
                }
            }
        }

        Film film = FilmMapper.mapToFilm(newFilmRequest);
        if (!givenFilmGenres.isEmpty()) {
            film.setGenres(givenFilmGenres);
        }
        film.setMpaRating(mpaRepository.getById(newFilmRequest.getMpa().get("id"))
                                .orElseThrow(() -> new NotFoundException("Такого рейтинга нет в базе данных")));
        film = filmRepository.create(film);
        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public FilmDto updateFilm(UpdateFilmRequest updateFilmRequest) {
        Film updatedFilm = filmRepository.getById(updateFilmRequest.getId())
                .map(film -> FilmMapper.updateFilmFields(film, updateFilmRequest))
                .orElseThrow(() -> new NotFoundException("Такого фильма нет в базе данных"));

        List<Genre> requestGenres = genreRepository.getAll().stream().toList();
        List<Genre> givenFilmGenres = new ArrayList<>();
        for (int i = 0; i < updateFilmRequest.getGenres().size(); i++) {
            Map<String, String> genreMap = updateFilmRequest.getGenres().get(i);
            String genreId = genreMap.get("id");
            if (Integer.parseInt(genreId) > requestGenres.size()) {
                throw new NotFoundException("Такого жанра нет в базе данных");
            } else {
                for (Genre genre : requestGenres) {
                    if (genre.getId().toString().equals(genreId) && !givenFilmGenres.contains(genre)) {
                        givenFilmGenres.add(genre);
                        break;
                    }
                }
            }
        }

        if (!givenFilmGenres.isEmpty()) {
            updatedFilm.setGenres(givenFilmGenres);
        }
        updatedFilm = filmRepository.update(updatedFilm);
        return FilmMapper.mapToFilmDto(updatedFilm);
    }

    @Override
    public FilmDto getById(long filmId) {
        Film film = filmRepository.getById(filmId)
                .orElseThrow(() -> new NotFoundException("Такого фильма нет в базе данных"));
        film.setGenres(genreRepository.getFilmGenres(filmId));
        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public Collection<FilmDto> getAllFilms() {
        Collection<Film> films = filmRepository.getAllFilms();
        return extractFilmListToDto(films);
    }

    // работа с лайками
    @Override
    public void addLike(long filmId, long userId) {
        Film film = filmRepository.getById(filmId)
                .orElseThrow(() -> new NotFoundException("Такого фильма нет в базе данных"));
        User user = userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));

        filmRepository.addLike(userId, filmId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        Film film = filmRepository.getById(filmId)
                .orElseThrow(() -> new NotFoundException("Такого фильма нет в базе данных"));
        User user = userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));

        filmRepository.removeLike(userId, filmId);
    }

    @Override
    public Collection<FilmDto> getFilmsWithMostLikes(int count) {
        Collection<Film> films = filmRepository.getFilmsWithMostLikes(count);
        return films.stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    // "сервисы"
    private Collection<FilmDto> extractFilmListToDto(Collection<Film> films) {
        List<Genre> requestGenres = genreRepository.getAll().stream().toList();
        for (Film film : films) {
            List<Genre> givenFilmGenres = new ArrayList<>();
            for (Genre genre : film.getGenres()) {
                Integer genreId = genre.getId();
                for (Genre availableGenre : requestGenres) {
                    if (genreId.equals(availableGenre.getId()) && !givenFilmGenres.contains(availableGenre)) {
                        givenFilmGenres.add(availableGenre);
                        break;
                    }
                }
            }
        }

        return films
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }
}
