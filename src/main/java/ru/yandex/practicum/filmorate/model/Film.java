package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Film.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private long id;
    @NotEmpty
    private String name;
    @Size(max = 200, message = "описание не должно превышать 200 символов")
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Positive(message = "продолжительность не может быть отрицательным числом")
    private Long duration;
    private List<Like> likes;
    private Mpa mpa;
    private List<Genre> genres;
}
