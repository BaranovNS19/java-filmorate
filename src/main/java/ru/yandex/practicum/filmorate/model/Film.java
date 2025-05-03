package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
public class Film {
    private long id;
    @NotEmpty
    private String name;
    @Size(max = 200, message = "описание не должно превышать 200 символов")
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "продолжительность не может быть отрицательным числом")
    private Long duration;
    private Set<Like> likes;
}
