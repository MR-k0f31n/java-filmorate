package ru.yandex.practicum.filmorate.storage.inMemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDao;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserDao implements UserDao {
    private long currencyUserId = 1;
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> usageEmail = new HashSet<>();

    @Override
    public List<User> getAllUser() {
        if (users.size() == 0) {
            log.info("Запрошен список пользователей, список пуст");
        }
        log.info("Запрошен список пользователей, вернули '{}' пользователей", users.size());
        return List.copyOf(users.values());
    }

    @Override
    public User addUser(User user) {
        checkEmail(user.getEmail());
        if (user.getName().equals("")) {
            user.setName(user.getLogin());
            log.info("У пользователя не задан name, был присвоен name: '{}'", user.getName());
        }
        user.setId(currencyUserId++);
        users.put(user.getId(), user);
        usageEmail.add(user.getEmail());
        log.info("Пользователь с email '{}' успешно создан, login '{}', присвоен id '{}'",
                user.getEmail(), user.getLogin(), user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!checkUser(user.getId())) {
            throw new NotFoundException("Пользователь не обнаружен id " + user.getId());
        }
        checkEmail(user.getEmail());
        users.put(user.getId(), user);
        log.info("Пользователь id '{}' успешно обновлен",
                user.getId());
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        if (!checkUser(id)) {
            throw new NotFoundException("Пользователь не обнаружен id " + id);
        }
        log.info("Пользователь id login '{} {}' удален", id, users.get(id));
        users.remove(id);
    }

    @Override
    public User getUserById(Long id) {
        if (!checkUser(id)) {
            throw new NotFoundException("Пользователь не обнаружен id " + id);
        }
        log.trace("Пользователь с id '{}' обнаружен", id);
        return users.get(id);
    }

    private boolean checkUser (Long id) {
        log.trace("check user id '{}'", id);
        return users.containsKey(id);
    }

    private void checkEmail(String email) {
        if (usageEmail.contains(email)) {
            log.warn("Пользователь с email '{}' уже существует", email);
            throw new ValidationException("Пользователь с таким email уже существует");
        }
    }
}
