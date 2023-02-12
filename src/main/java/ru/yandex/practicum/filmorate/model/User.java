package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;

@Data
@Builder
@NonNull
public class User {

    private int id;
    @NotBlank(message = "Поле email не может быть пустым")
    @NonNull
    private final String email;
    @NotBlank(message = "Поле login не может быть пустым")
    @NonNull
    private final String login;
    private String name;
    private final LocalDate birthday;
}
