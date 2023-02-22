package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private int currencyUserId = 1;
    private final Map<Integer, User> users = new HashMap<>();
    private final Set<String> usageEmail = new HashSet<>();

    @Override
    public List<User> getAllUser() {
        return List.copyOf(users.values());
    }

    @Override
    public User addUser(User user) {
        if (usageEmail.contains(user.getEmail())) {
            log.warn("Пользователь с email '{}' уже существует", user.getEmail());
            throw new ValidationException("Пользователь с таким email уже существует");
        }
        if (user.getName().equals("")) {
            user.setName(user.getLogin());
            log.info("У пользователя не задан name, был присвоен name: '{}'", user.getName());
        }
        user.setId(currencyUserId++);
        users.put(user.getId(), user);
        usageEmail.add(user.getEmail());
        log.info("Получен запрос к эндпоинту: 'POST /users', пользователь с email '{}' успешно создан, login '{}', присвоен id '{}'",
                user.getEmail(), user.getLogin(), user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
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

    @Override
    public void deleteUser(int id) {

    }

    @Override
    public User findUser(User user) {
        return null;
    }

    @Override
    public List<User> getUserFriend(User user) {
        return null;
    }

    @Override
    public List<Film> findFilmUserLike(User user) {
        return null;
    }
}
