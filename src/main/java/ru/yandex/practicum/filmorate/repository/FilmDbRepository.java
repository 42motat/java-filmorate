/*
 * Александру Ф.
 * Добрый день!
 * Заранее благодарю за код-ревью.
 * */

package ru.yandex.practicum.filmorate.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.Optional;

@Primary
@Repository
public class FilmDbRepository extends BaseDbRepository<Film> implements FilmStorage {

    // шаблоны запросов
    private static final String GET_ALL_QUERY = """
            SELECT f.*, mpa.ratingName
            FROM films f
            LEFT JOIN mpaRating mpa ON f.ratingId = mpa.ratingId
            """;

    private static final String GET_BY_ID_QUERY = """
            SELECT f.filmId, f.name, f.description, f.releaseDate, f.duration, f.ratingId, mpa.ratingName
            FROM films f
            LEFT JOIN mpaRating mpa ON f.ratingId = mpa.ratingId
            WHERE filmId = ?
            """;

    private static final String CREATE_QUERY = """
            INSERT INTO films (name, description, releaseDate, duration, ratingId) VALUES (?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_QUERY = """
            UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, ratingId = ?
            WHERE filmId = ?
            """;
    private static final String INSERT_INTO_FILM_GENRES_QUERY = """
            INSERT INTO filmGenres (filmId, genreId) VALUES (?, ?)
            """;

    private static final String DELETE_QUERY = """
            SELECT * FROM films where filmId = ?
            """;

    private static final String ADD_LIKE_QUERY = """
            INSERT INTO usersWhoLiked (userId, filmId) VALUES (?, ?)
            """;

    private static final String REMOVE_LIKE_QUERY = """
            DELETE FROM usersWhoLiked WHERE userId = ? AND filmId = ?
            """;

    private static final String GET_MOST_LIKES_QUERY = """
            SELECT f.filmId, f.name, f.description, f.releaseDate, f.duration, f.ratingId, mpa.ratingName
            FROM films f
            LEFT JOIN usersWhoLiked uwl ON f.filmId = uwl.filmId
            LEFT JOIN mpaRating mpa ON f.ratingId = mpa.ratingId
            GROUP BY f.filmId
            ORDER BY COUNT(uwl.userId) DESC
            LIMIT ?
            """;

    // конструктор
    public FilmDbRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    // стандартные операции
    @Override
    public Collection<Film> getAllFilms() {
        return getAll(GET_ALL_QUERY);
    }

    @Override
    public Optional<Film> getById(long filmId) {
        return getOne(GET_BY_ID_QUERY, filmId);
    }

    @Override
    public Film create(Film film) {
        FilmValidator.validate(film);
        long filmId = insert(
                CREATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpaRating().getId()
        );
        film.setId(filmId);
        film.getGenres().forEach(genre -> setFilmGenres(filmId, genre.getId()));
        return film;
    }

    @Override
    public Film update(Film film) {
        insert(UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpaRating().getId(),
                film.getId()
        );
        film.getGenres().forEach(genre -> setFilmGenres(film.getId(), genre.getId()));
        return film;
    }

    @Override
    public void deleteById(long filmId) {
        delete(DELETE_QUERY, filmId);
    }

    // лайки
    @Override
    public void addLike(long userId, long filmId) {
        update(ADD_LIKE_QUERY, userId, filmId);
    }

    @Override
    public void removeLike(long userId, long filmId) {
        delete(REMOVE_LIKE_QUERY, userId, filmId);
    }

    @Override
    public Collection<Film> getFilmsWithMostLikes(int count) {
        return getAll(GET_MOST_LIKES_QUERY, count);
    }

    // "сервисы"
    private void setFilmGenres(Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(INSERT_INTO_FILM_GENRES_QUERY, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        }, keyHolder);
    }
}
