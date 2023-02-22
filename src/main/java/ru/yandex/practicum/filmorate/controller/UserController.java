package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final InMemoryUserStorage userStorage;

    @GetMapping
    public List<User> returnAllUser() {
        List<User> users = userStorage.getAllUser();
        log.info("Получен запрос к эндпоинту: 'GET /users', Вернули список пользователей: '{}'",
                users.size());
        return users;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userStorage.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userStorage.updateUser(user);
    }
}
