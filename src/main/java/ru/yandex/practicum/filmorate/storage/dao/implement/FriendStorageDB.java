package ru.yandex.practicum.filmorate.storage.dao.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.storage.dao.Friends;

import java.util.List;

@Component
@Primary
@Slf4j
@RequiredArgsConstructor
public class FriendStorageDB implements Friends {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Long userId, Long friendId) {
        String sqlRequest = "INSERT INTO friends (user_id, friend_id, status)" +
                "VALUES (?,?, FALSE)";
        jdbcTemplate.update(sqlRequest, userId, friendId);

        sqlRequest = "SELECT count(*) FROM friends WHERE user_id = friend_id and friend_id = user_id";
        Integer checkFriendship = jdbcTemplate.queryForObject(sqlRequest, Integer.class, userId, friendId);
        if (checkFriendship > 1) {
            sqlRequest = "UPDATE friends SET status = TRUE where user_id = ? and friend_id = ?";
            jdbcTemplate.update(sqlRequest, userId, friendId);
            jdbcTemplate.update(sqlRequest, friendId, userId);
        }
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        String sqlRequest = "DELETE FROM friends WHERE user_id = ?";
        jdbcTemplate.update(sqlRequest, userId);

        sqlRequest = "UPDATE friends SET status = FALSE where friend_id = ?";
        jdbcTemplate.update(sqlRequest, friendId);
    }

    @Override
    public List<User> getAllFriendUserById(Long userId) {
        String sqlQuery = "select U.* " +
                "from friends as FS " +
                "join users as U on FS.friend_id = U.user_id " +
                "where FS.user_id = ?";

        return jdbcTemplate.query(sqlQuery,new UserRowMapper(), userId);
    }

    @Override
    public List<Boolean> checkStatus(Long userId, Long friendId) {
        return null;
    }
}
