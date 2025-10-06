package ru.yandex.practicum.filmorate.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreDbRepository extends BaseDbRepository<Genre> {

    // шаблоны запросов
    private static final String GET_ALL_GENRES = """
            SELECT genreId, name FROM genres
            """;

    private static final String GET_BY_ID_GENRE = """
            SELECT * FROM genres WHERE genreId = ?
            """;

    private static final String GET_FILM_GENRES = """
            SELECT g.genreId, g.name
            FROM genres g
            JOIN filmGenres fg ON g.genreId = fg.genreId
            WHERE fg.filmId = ?
            ORDER BY g.genreId
            """;

    public GenreDbRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Genre> getAll() {
        return getAll(GET_ALL_GENRES).stream()
                .sorted(Comparator.comparingInt(Genre::getId))
                .toList();
    }

    public Optional<Genre> getById(int genreId) {
        return getOne(GET_BY_ID_GENRE, genreId);
    }

    public List<Genre> getFilmGenres(long filmId) {
        return jdbc.query(GET_FILM_GENRES, mapper, filmId);
    }
}
