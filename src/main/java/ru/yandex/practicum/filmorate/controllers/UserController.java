package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private int currencyUserId = 1;
    private final Map<Integer, User> users = new HashMap<>();
    private final Set<String> usageEmail = new HashSet<>();

    @GetMapping
    public List<User> returnAllUser() {
        log.info("Получен запрос к эндпоинту: 'GET /users', Вернули список пользователей: '{}'",
                users.size());
        return List.copyOf(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if (usageEmail.contains(user.getEmail())) {
            log.warn("Пользователь с email '{}' уже существует", user.getEmail());
            throw new ValidationException("Пользователь с таким email уже существует");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(currencyUserId++);
        users.put(user.getId(), user);
        usageEmail.add(user.getEmail());
        log.info("Получен запрос к эндпоинту: 'POST /users', пользователь с email '{}' успешно создан, login '{}', присвоен id '{}'",
                user.getEmail(), user.getLogin(), user.getId());
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.debug("Получен запрос к эндпоинту: 'PUT /users', пользователь с id '{}' не существует",
                    user.getId());
            throw new NotFoundException("Пользователь с таким id не обнаружен!");
        }
        if (usageEmail.contains(user.getEmail())) {
            log.debug("Получен запрос к эндпоинту: 'PUT /users', такой эмейл уже существует '{}'",
                    user.getEmail());
            throw new ValidationException("Пользователь с таким email существует");
        }
        users.put(user.getId(), user);
        log.info("Получен запрос к эндпоинту: 'PUT /users', пользователь id '{}' успешно обновлен",
                user.getId());
        return user;
    }
}
