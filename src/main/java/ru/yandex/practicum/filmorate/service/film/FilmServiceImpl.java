package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    // работа с лайками
    @Override
    public void addLike(long filmId, long userId) {
        Film film = filmStorage.getById(filmId).orElseThrow(() -> new NotFoundException("Такого фильма нет в базе данных"));
        User user = userStorage.getById(userId).orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));

        film.getUsersWhoLiked().add(userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        Film film = filmStorage.getById(filmId).orElseThrow(() -> new NotFoundException("Такого фильма нет в базе данных"));
        User user = userStorage.getById(userId).orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));

        film.getUsersWhoLiked().remove(userId);
    }

    @Override
    public Collection<Film> getFilmsWithMostLikes(int count) {
        List<Film> films = new ArrayList<>(filmStorage.getAllFilms());
        films.sort(Collections.reverseOrder(Comparator.comparing(film -> film.getUsersWhoLiked().size())));
        return films.stream()
                .limit(count)
                .toList();
    }
}
