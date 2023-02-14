package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
@NonNull
public class User {
    @Positive
    private Integer id;
    @Email(message = "Поле email не соответствует формату userEmail@email.com")
    private final String email;
    @Pattern(message = "Поле login должно содержать только A-Z и 1-0",
            regexp = "^[A-Za-z]([.A-Za-z0-9-]{1,18})([A-Za-z0-9])$")
    private final String login;

    @Builder.Default
    private String name = "";
    @PastOrPresent(message = "Поле birthday не корректно")
    private final LocalDate birthday;
}
