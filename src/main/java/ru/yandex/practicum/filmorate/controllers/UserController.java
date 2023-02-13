package ru.yandex.practicum.filmorate.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private int currencyUserId = 1;
    private final Map<Integer, User> users = new HashMap<>();
    private final Set<String> usageEmail = new HashSet<>();

    @GetMapping
    public List<User> returnAllUser(HttpServletRequest request) {
        log.trace("Получен запрос к эндпоинту: '{} {}', Вернули список пользователей: '{}'",
                request.getMethod(), request.getRequestURI(), users.size());
        return List.copyOf(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user, HttpServletRequest request) throws ValidationException {
        if (usageEmail.contains(user.getEmail())) {
            log.debug("Пользователь с email '{}' уже существует", user.getEmail());
            throw new ValidationException("Пользователь с таким email уже существует");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(currencyUserId++);
        users.put(user.getId(), user);
        usageEmail.add(user.getEmail());
        log.info("Получен запрос к эндпоинту: '{} {}', пользователь с email '{}' успешно создан, login '{}', присвоен id '{}'",
                request.getMethod(), request.getRequestURI(),
                user.getEmail(), user.getLogin(), user.getId());
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user, HttpServletRequest request) throws ValidationException {
        if (!users.containsKey(user.getId())) {
            log.debug("Получен запрос к эндпоинту: '{} {}', пользователь с id '{}' не существует",
                    request.getMethod(), request.getRequestURI(), user.getId());
            throw new NotFoundException("Пользователь с таким id не обнаружен!");
        }
        users.put(user.getId(), user);
        return user;
    }
}
