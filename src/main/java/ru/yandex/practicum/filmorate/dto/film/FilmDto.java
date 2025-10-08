package ru.yandex.practicum.filmorate.dto.film;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class FilmDto {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;
    private List<Genre> genres = new ArrayList<>();
    private MpaRating mpa;
}
