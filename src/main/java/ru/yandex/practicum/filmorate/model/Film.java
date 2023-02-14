package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
@NonNull
public class Film {
    private final static int MAX_LENGTH_DESCRIPTION = 200;
    @Positive
    private Integer id;
    @NotBlank(message = "Поле name не может быть пустым")
    private final String name;
    @NotBlank(message = "Поле description не может быть пустым")
    @Size(message = "Поле description имеет максимальное число символов: " + MAX_LENGTH_DESCRIPTION,
            max = MAX_LENGTH_DESCRIPTION)
    private final String description;
    @PastOrPresent(message = "Поле releaseDate некорректно")
    private final LocalDate releaseDate;
    @Positive(message = "должно быть больше 0")
    private final Integer duration;
}
