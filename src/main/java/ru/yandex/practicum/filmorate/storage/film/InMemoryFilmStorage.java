package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.debug("Фильм успешно добавлен в список фильмов; id={}", film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        if (film.getId() == null) {
            throw new NotFoundException("Необходимо указать id фильма," +
                    " иначе его невозможно будет найти в нашей базе");
        }

        if (films.containsKey(film.getId())) {
            Film oldFilm = films.get(film.getId());
            // валидация проходит раньше поиска, поэтому можно считать все переданные значения валидными
            oldFilm.setName(film.getName());
            oldFilm.setDescription(film.getDescription());
            oldFilm.setReleaseDate(film.getReleaseDate());
            oldFilm.setDuration(film.getDuration());

            return oldFilm;
        }
        throw new NotFoundException("Указанный фильм не найден");
    }

    @Override
    public Optional<Film> getById(long filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public Film deleteById(long filmId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("Проверьте корректность введённого id фильма");
        }
        return films.remove(filmId);
    }

    public long getNextId() {
        long currentMaxId = films.keySet().stream()
                .mapToLong(filmId -> filmId)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
