package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Duration;
import java.time.LocalDate;

@Data
@Builder
@NonNull
public class Film {

    private int id;
    @NonNull
    @NotBlank(message = "Поле name не может быть пустым")
    private final String name;
    @NonNull
    @NotBlank(message = "Поле description не может быть пустым")
    private final String description;
    @NonNull
    private final LocalDate releaseDate;
    @NonNull
    private final Duration duration;
}
