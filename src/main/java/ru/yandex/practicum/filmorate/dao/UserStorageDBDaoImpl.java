package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.SQLException;
import java.util.*;

@Component
@Primary
@Slf4j
@RequiredArgsConstructor
public class UserStorageDBDaoImpl implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAllUser() {
        String sqlRequest = "SELECT * FROM users";
        return jdbcTemplate.query(sqlRequest, new UserRowMapper());
    }

    @Override
    public User addUser(User user) {
        String sqlRequest = "INSERT INTO users (email, login, name, birthday) VALUES (?,?,?,?)";
        jdbcTemplate.update
                (
                        sqlRequest,
                        user.getEmail(),
                        user.getLogin(),
                        user.getName(),
                        user.getBirthday()
                );
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!checkUser(user.getId())) {
            log.warn("User not found id = '{}'", user.getId());
            throw new NotFoundException("User not found id = {" + user.getId() + "}");
        }
        String sqlRequest = "INSERT INTO (user_id, email, login, name, birthday) users VALUES (?,?,?,?,?)";
        getUserById(user.getId());
        jdbcTemplate.update
                (
                        sqlRequest,
                        user.getId(),
                        user.getEmail(),
                        user.getLogin(),
                        user.getName(),
                        user.getBirthday()
                );
        return getUserById(user.getId());
    }

    @Override
    public void deleteUser(Long id) {
        if (!checkUser(id)) {
            log.warn("User not found id = '{}'", id);
            throw new NotFoundException("User not found id = {" + id + "}");
        }
        String sqlRequest = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sqlRequest, id);
    }

    @Override
    public User getUserById(Long id) {
        if (!checkUser(id)) {
            log.warn("User not found id = '{}'", id);
            throw new NotFoundException("User not found id = {" + id + "}");
        }
        String sqlRequest = "SELECT * FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sqlRequest, new UserRowMapper(), id);
    }

    @Override
    public boolean checkUser(Long id) {
        String sqlRequest = "SELECT * FROM users WHERE user_id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sqlRequest, new UserRowMapper(), id)).isPresent();
    }
}
