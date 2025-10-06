package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreDbRepository;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
public class GenreServiceTest {
    @Autowired
    GenreDbRepository genreRepository;

    @Test
    public void getAllGenresTest() {
        Collection<Genre> genreList = genreRepository.getAll();

        assertThat(genreList).asList().size().isEqualTo(6);
    }

}
