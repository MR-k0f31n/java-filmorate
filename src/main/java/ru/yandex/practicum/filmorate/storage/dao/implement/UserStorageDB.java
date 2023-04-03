package ru.yandex.practicum.filmorate.storage.dao.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.mapper.FriendRowMapper;
import ru.yandex.practicum.filmorate.storage.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Primary
@Slf4j
@RequiredArgsConstructor
public class UserStorageDB implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final FriendStorageDB friendStorageDB;

    @Override
    public List<User> getAllUser() {
        String sqlRequest = "SELECT u.USER_ID, u.EMAIL, u.LOGIN, u.NAME, u.BIRTHDAY, FRIEND_ID " +
                "FROM users u " +
                "LEFT JOIN FRIENDS f ON u.USER_ID = f.USER_ID";
        log.info("Вернуть список всех пользователей размер списка ");
        return jdbcTemplate.query(sqlRequest, listResultSetExtractor);
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
            user.getFriends().addAll(getFriendUserById(id));
            log.info("Найден user с id '{}' имя '{}'", id, user.getName());
            return user;
        } catch (Throwable exception) {
            log.warn("Не удалось найти пользователя id = '{}'", id);
            throw new NotFoundException("Не удалось найти пользователя id = " + id);
        }
    }

    private Set<Long> getFriendUserById(Long id) {
        String sqlQuery = "SELECT friend_id FROM friends WHERE user_id = ?";
        log.info("Вернули весь список друзей пользователя id '{}'", id);
        return new HashSet<>(jdbcTemplate.query(sqlQuery, new FriendRowMapper(), id));
    }

    private final ResultSetExtractor<List<User>> listResultSetExtractor = rs -> {
        Map<Long, User> userMap = new HashMap<>();
        User user;
        while (rs.next()) {
            Long userId = rs.getLong("user_id");
            user = userMap.get(userId);
            if (user == null) {
                user = new User(
                        rs.getLong("user_id"),
                        rs.getString("email"),
                        rs.getString("login"),
                        rs.getString("name"),
                        rs.getDate("birthday").toLocalDate()
                );
                userMap.put(userId, user);
            }
            Set<Long> userFriend = user.getFriends();
            if (rs.getLong("friend_id") != 0) {
                userFriend.add(rs.getLong("friend_id"));
            }
        }
        return new ArrayList<>(userMap.values());
    };
}
