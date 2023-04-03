package ru.yandex.practicum.filmorate.storage.dao.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.mapper.UserLikeRowMapper;
import ru.yandex.practicum.filmorate.storage.dao.FilmLikeDao;

import java.util.HashSet;
import java.util.Set;

@Component
@Primary
@Slf4j
@RequiredArgsConstructor
public class FilmLikeDB implements FilmLikeDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(Long filmId, Long userId) {
        String sqlRequest = "INSERT INTO film_like (film_id, user_like)" +
                "VALUES (?,?)";
        jdbcTemplate.update(sqlRequest, filmId, userId);
        log.info("Добавлен Like фильму id '{}' от пользователя id '{}'", filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        String sqlRequest = "DELETE FROM film_like WHERE film_id = ? AND user_like = ? ";
        jdbcTemplate.update(sqlRequest, filmId, userId);
        log.info("Like удален фильм id '{}' от пользователь id '{}'", filmId, userId);
    }

    @Override
    public Set<Long> userLikeByFilmId(Long filmId) {
        String sqlRequest = "SELECT user_like FROM film_like " +
                "WHERE film_id = ?";
        log.info("Найдены все Like для фильма id '{}'", filmId);
        return new HashSet<>(jdbcTemplate.query(sqlRequest, new UserLikeRowMapper(), filmId));
    }
}
