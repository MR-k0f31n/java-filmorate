package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.validator.customInterface.DateAfterCinemaBirthday;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
@AllArgsConstructor
@Builder
public class Film {
    @Positive
    private Long id;
    @NotBlank(message = "Поле name не может быть пустым")
    private String name;
    @NotBlank(message = "Поле description не может быть пустым")
    @Size(message = "Поле description имеет максимальное число символов: " + 200,
            max = 200)
    private String description;
    @PastOrPresent(message = "Поле releaseDate некорректно")
    @DateAfterCinemaBirthday(message = "Поле releaseDate некорректно дата первого кино: 28.12.1895")
    private LocalDate releaseDate;
    @Positive(message = "должно быть больше 0")
    private Long duration;
    private final Set<Genre> genres = new TreeSet<>(Comparator.comparingLong(Genre::getId));
    private final Set<Long> userLike = new HashSet<>();
    private Mpa mpa;
}
