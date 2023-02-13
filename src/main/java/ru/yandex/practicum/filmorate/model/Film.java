package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDate;

@Data
@Builder
@NonNull
public class Film {
    private final static int MAX_LENGTH_DESCRIPTION = 200;
    private int id;
    @NonNull
    @NotBlank(message = "Поле name не может быть пустым")
    private final String name;
    @NonNull
    @NotBlank(message = "Поле description не может быть пустым")
    @Size(message = "Поле description имеет максимальноче число символов: " + MAX_LENGTH_DESCRIPTION,
            max = MAX_LENGTH_DESCRIPTION)
    private final String description;
    @NonNull
    @PastOrPresent(message = "Поле releaseDate некореткто")
    private final LocalDate releaseDate;
    @NonNull
    @Positive
    private final int duration;
}
