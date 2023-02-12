package ru.yandex.practicum.filmorate.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private int currencyUserId = 0;
    private final Map<String, User> users = new HashMap<>();

    @GetMapping
    public List<User> returnAllUser(HttpServletRequest request) {
        log.trace("Получен запрос к эндпоинту: '{} {}', Вернули список пользователей: '{}'",
                request.getMethod(), request.getRequestURI(), users.size());
        return List.copyOf(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user, HttpServletRequest request) throws ValidationException {
        LocalDate date = LocalDate.now();

        @Email(message = "Поле email не соответствует формату @email",
                regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
                        "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]" +
                        "|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])" +
                        "?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.)" +
                        "{3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:" +
                        "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f" +
                        "])+)\\])")
        String userEmail = user.getEmail();
        if (users.containsKey(userEmail)) {
            log.debug("Получен запрос к эндпоинту: '{} {}', пользователь с email '{}' уже существует",
                    request.getMethod(), request.getRequestURI(), userEmail);
            throw new ValidationException("Пользователь с таким email существует");
        }
        if (user.getBirthday().isAfter(date)) {
            log.debug("Получен запрос к эндпоинту: '{} {}', пользователь указал дату рождения '{}'",
                    request.getMethod(), request.getRequestURI(), user.getBirthday());
            throw new ValidationException("Возможно вы из будущего и мы не сможем найти интересующий вас фильм");
        }
        if (user.getName().isBlank() || user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(currencyUserId++);
        log.info("Получен запрос к эндпоинту: '{} {}', пользователь с email '{}' успешно создан, login '{}', присвоен id '{}'",
                request.getMethod(), request.getRequestURI(),
                user.getEmail(), user.getLogin(), user.getId());
        users.put(userEmail, user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user, HttpServletRequest request) throws ValidationException {

        String userEmail = user.getEmail();
        if (!users.containsKey(userEmail)) {
            log.debug("Получен запрос к эндпоинту: '{} {}', пользователь с email '{}' не существует",
                    request.getMethod(), request.getRequestURI(), userEmail);
            throw new ValidationException("Пользователь с таким email не обнаружен!");
        }
        users.put(userEmail, user);
        return user;
    }
}
