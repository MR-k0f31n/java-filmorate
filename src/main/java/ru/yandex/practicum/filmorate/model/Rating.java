package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@NonNull
public class Rating {
    @Positive
    private final Long id;
    @NotBlank
    private String name;
}
