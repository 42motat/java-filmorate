package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.repository.MpaRatingDbRepository;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaRatingController {
    private final MpaRatingDbRepository mpaRepository;

    @GetMapping
    public Collection<MpaRating> getAllMpaRatings() {
        return mpaRepository.getAll();
    }

    @GetMapping("/{id}")
    public Optional<MpaRating> getMpaRatingById(@PathVariable int id) {
        return Optional.ofNullable(mpaRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("Такого рейтинга нет в базе данных")));
    }
}
