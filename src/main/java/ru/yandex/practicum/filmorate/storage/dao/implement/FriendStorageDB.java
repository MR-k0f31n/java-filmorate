package ru.yandex.practicum.filmorate.storage.dao.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.mapper.FriendRowMapper;
import ru.yandex.practicum.filmorate.model.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.storage.dao.Friends;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Primary
@Slf4j
@RequiredArgsConstructor
public class FriendStorageDB implements Friends {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Long userId, Long friendId) {
        String sqlRequest = "INSERT INTO friends (user_id, friend_id)" +
                "VALUES (?,?)";
        jdbcTemplate.update(sqlRequest, userId, friendId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        String sqlRequest = "DELETE FROM friends WHERE user_id = ? AND friend_id = ? ";
        jdbcTemplate.update(sqlRequest, userId);
    }

    @Override
    public HashSet<Long> getAllFriendUserById(Long userId) {
        String sqlQuery = "SELECT friend_id FROM friends WHERE user_id = ?";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, new FriendRowMapper(), userId));
    }

    @Override
    public List<Boolean> checkStatus(Long userId, Long friendId) {
        return null;
    }
}
