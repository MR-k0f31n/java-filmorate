package ru.yandex.practicum.filmorate.validations;

import jakarta.validation.Valid;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;

@SpringBootTest
public class UserValidatorTest {

    UserController us = new UserController();

    @Test
    void loginWithBlank_expectedError () {

        User user= User.builder()
                .login("commono")
                .email("commono@gragon.net")
                .birthday(LocalDate.now().minusDays(1))
                .build();

       // Assertions.assertThrows();
    }
}
