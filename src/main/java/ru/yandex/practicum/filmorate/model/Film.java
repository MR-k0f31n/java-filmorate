package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.validator.customInterface.DateAfterCinemaBirthday;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class Film {
    private final static int MAX_LENGTH_DESCRIPTION = 200;
    @Positive
    private Long id;
    @NotBlank(message = "Поле name не может быть пустым")
    private String name;
    @NotBlank(message = "Поле description не может быть пустым")
    @Size(message = "Поле description имеет максимальное число символов: " + MAX_LENGTH_DESCRIPTION,
            max = MAX_LENGTH_DESCRIPTION)
    private String description;
    @PastOrPresent(message = "Поле releaseDate некорректно")
    @DateAfterCinemaBirthday(message = "Поле releaseDate некорректно дата первого кино: 28.12.1895")
    private LocalDate releaseDate;
    @Positive(message = "должно быть больше 0")
    private Long duration;
    private Set<Genre> genre;
    private Set<User> userLike;
    private Mpa mpa;
}
