package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NonNull
public class User {

    private int id;
    @NotBlank(message = "Поле email не может быть пустым")
    @Email(message = "Поле email не соответствует формату userEmail@email.com")
    private final String email;
    @NotBlank(message = "Поле login не может быть пустым")
    @Pattern(message = "Поле login должно содежать только A-Z и 1-0",
            regexp = "^[A-Za-z]([.A-Za-z0-9-]{1,18})([A-Za-z0-9])$")
    private final String login;

    @Builder.Default
    private String name = "";
    @PastOrPresent(message = "Поле birthday не корректно")
    private final LocalDate birthday;
}
