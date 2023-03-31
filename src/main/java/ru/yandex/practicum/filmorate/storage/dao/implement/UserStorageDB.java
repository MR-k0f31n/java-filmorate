package ru.yandex.practicum.filmorate.storage.dao.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
@Primary
@Slf4j
@RequiredArgsConstructor
public class UserStorageDB implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final FriendStorageDB friendStorageDB;

    @Override
    public List<User> getAllUser() {
        String sqlRequest = "SELECT * FROM users";
        List<User> users = jdbcTemplate.query(sqlRequest, new UserRowMapper());
        users.forEach(id -> {
            id.getFriends().addAll(friendStorageDB.getAllFriendUserById(id.getId()));
        });
        log.info("Вернуть список всех пользователей размер списка '{}'", users.size());
        return users;
    }

    @Override
    public User addUser(User user) {
        String sqlRequest = "INSERT INTO users (email, login, name, birthday) VALUES (?,?,?,?)";
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("У user пустое имя, присвоен вместо имени присвоен login '{}'", user.getLogin());
        }
        jdbcTemplate.update(
                sqlRequest,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        sqlRequest = "SELECT * FROM users WHERE login = ?";
        log.info("User успешно создан user login '{}'", user.getLogin());
        return jdbcTemplate.queryForObject(sqlRequest, new UserRowMapper(), user.getLogin());
    }

    @Override
    public User updateUser(User user) {
        getUserById(user.getId());
        String sqlRequest = "UPDATE users " +
                "SET email = ?, login = ?, name = ?, birthday = ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(
                sqlRequest,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        log.info("User id '{}' успешно обновлен", user.getId());
        return getUserById(user.getId());
    }

    @Override
    public void deleteUser(Long id) {
        getUserById(id);
        String sqlRequest = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sqlRequest, id);
        log.info("User id '{}' успешно удален", id);
    }

    @Override
    public User getUserById(Long id) {
        try {
            String sqlRequest = "SELECT * FROM users WHERE user_id = ?";
            User user = jdbcTemplate.queryForObject(sqlRequest, new UserRowMapper(), id);
            user.getFriends().addAll(friendStorageDB.getAllFriendUserById(id));
            log.info("Найден user с id '{}' имя '{}'", id, user.getName());
            return user;
        } catch (Throwable exception) {
            log.warn("Не удалось найти пользователя id = '{}'", id);
            throw new NotFoundException("Не удалось найти пользователя id = " + id);
        }
    }
}
