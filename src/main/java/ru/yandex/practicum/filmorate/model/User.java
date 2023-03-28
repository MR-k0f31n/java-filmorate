package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class User {
    @Positive
    private Long id;
    @Email(message = "Поле email не соответствует формату userEmail@email.com")
    private String email;
    @Pattern(message = "Поле login должно содержать только A-Z и 1-0",
            regexp = "^[A-Za-z]([.A-Za-z0-9-]{1,18})([A-Za-z0-9])$")
    @NonNull
    private String login;
    private String name;
    @PastOrPresent(message = "Поле birthday не корректно")
    private LocalDate birthday;
    private Set<Long> friends;
}
