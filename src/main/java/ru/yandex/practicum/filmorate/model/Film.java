package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.validator.customInterface.DateAfterCinemaBirthday;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    @DateAfterCinemaBirthday(message = "Поле releaseDate некорректно дата первого кино: 28.12.1895")
    private final LocalDate releaseDate;
    @Positive(message = "должно быть больше 0")
    private final Integer duration;
    private final Set<Integer> userLike = new HashSet<>();
}
