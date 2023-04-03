package ru.yandex.practicum.filmorate.storage.dao.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.dao.FriendsDao;

@Component
@Primary
@Slf4j
@RequiredArgsConstructor
public class FriendStorageDB implements FriendsDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Long userId, Long friendId) {
        String sqlRequest = "INSERT INTO friends (user_id, friend_id)" +
                "VALUES (?,?)";
        jdbcTemplate.update(sqlRequest, userId, friendId);
        log.info("Добавление пользователя id '{}' в друзья к id '{}'", friendId, userId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        String sqlRequest = "DELETE FROM friends WHERE user_id = ? AND friend_id = ? ";
        jdbcTemplate.update(sqlRequest, userId, friendId);
        log.info("Удаление пользователя id '{}' из друзей у id '{}'", friendId, userId);
    }
}
