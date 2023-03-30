package ru.yandex.practicum.filmorate.storage.dao.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
@Primary
@Slf4j
@RequiredArgsConstructor
public class GenreStorageDB implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenreById(Long id) {
        try {
            String sqlRequest = "SELECT * FROM genres WHERE genre_id = ?";
            return jdbcTemplate.queryForObject(sqlRequest, new GenreRowMapper(), id);
        } catch (Throwable exception) {
            log.warn("Не удалось найти жанр id = '{}'", id);
            throw new NotFoundException("Не удалось найти жанр id = " + id);
        }
    }

    @Override
    public List<Genre> getGenres() {
        String sqlRequest = "SELECT * FROM genres ORDER BY genre_id ASC";
        return jdbcTemplate.query(sqlRequest, new GenreRowMapper());
    }
}
