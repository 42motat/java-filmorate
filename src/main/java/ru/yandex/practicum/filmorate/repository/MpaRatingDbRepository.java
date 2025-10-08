package ru.yandex.practicum.filmorate.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

@Repository
public class MpaRatingDbRepository extends BaseDbRepository<MpaRating> {

    // шаблоны запросов
    private static final String GET_ALL_MPARATINGS = """
            SELECT * FROM mpaRating
            """;

    private static final String GET_BY_ID_MPARATING = """
            SELECT * FROM mpaRating WHERE ratingId = ?
            """;

    public MpaRatingDbRepository(JdbcTemplate jdbc, RowMapper<MpaRating> mapper) {
        super(jdbc, mapper);
    }

    public Collection<MpaRating> getAll() {
        return getAll(GET_ALL_MPARATINGS).stream()
                .sorted(Comparator.comparingInt(MpaRating::getId))
                .toList();
    }

    public Optional<MpaRating> getById(int ratingId) {
        return getOne(GET_BY_ID_MPARATING, ratingId);
    }

}
