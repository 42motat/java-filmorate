package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.repository.MpaRatingDbRepository;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
public class MpaRatingServiceTest {
    @Autowired
    MpaRatingDbRepository mpaRepository;

    @Test
    public void getAllMPATest() {
        Collection<MpaRating> mpaList = mpaRepository.getAll();

        assertThat(mpaList).asList().size().isEqualTo(5);
    }
}
