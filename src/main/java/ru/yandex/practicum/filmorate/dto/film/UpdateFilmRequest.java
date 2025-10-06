package ru.yandex.practicum.filmorate.dto.film;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.*;

@Data
public class UpdateFilmRequest {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Min(1)
    private long duration;
    private Set<Long> usersWhoLiked = new HashSet<>();
    private List<Map<String, String>> genres = new ArrayList<>();
    private Map<String, Integer> mpa;

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasDesc() {
        return !(description == null || description.isBlank());
    }

    public boolean hasReleaseDate() {
        return !(releaseDate == null);
    }

    public boolean hasDuration() {
        return !(duration <= 0);
    }
}
