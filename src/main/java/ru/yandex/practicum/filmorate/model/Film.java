package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder
public class Film {
    private long id;
    @NotEmpty
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Long duration;
}
