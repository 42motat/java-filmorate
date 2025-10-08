package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreDbRepository;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreDbRepository genreRepository;

    @GetMapping
    public Collection<Genre> getAllGenres() {
        return genreRepository.getAll();
    }

    @GetMapping("/{id}")
    public Optional<Genre> getGenreById(@PathVariable int id) {
        return Optional.ofNullable(genreRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("Такого жанра нет в базе данных")));
    }
}
