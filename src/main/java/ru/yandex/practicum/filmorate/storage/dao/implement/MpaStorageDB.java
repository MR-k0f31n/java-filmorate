package ru.yandex.practicum.filmorate.storage.dao.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.mapper.MpaRowMapper;

import java.util.List;

@Component
@Primary
@Slf4j
@RequiredArgsConstructor
public class MpaStorageDB implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getMpaById(Long id) {
        try {
            String sqlRequest = "SELECT * FROM mpa WHERE mpa_id = ?";
            return jdbcTemplate.queryForObject(sqlRequest, new MpaRowMapper(), id);
        } catch (EmptyResultDataAccessException exception) {
            log.warn("Не удалось найти MPA рейтинг id = '{}'", id);
            throw new NotFoundException("Не удалось найти MPA рейтинг id = '" + id + "'");
        }
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sqlRequest = "SELECT * FROM mpa ORDER BY mpa_id ASC";
        return jdbcTemplate.query(sqlRequest, new MpaRowMapper());
    }
}
