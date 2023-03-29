package ru.yandex.practicum.filmorate.storage.dao.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
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
    JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getRatingById(Long id) {
        try {
            String sqlRequest = "SELECT * FROM ratings WHERE rating_id = ?";
            return jdbcTemplate.queryForObject(sqlRequest, new MpaRowMapper(), id);
        } catch (Throwable exception) {
            log.warn("Не удалось найти возрастной рейтинг id = '{}'", id);
            throw new NotFoundException("Не удалось найти возрастной рейтинг id = " + id);
        }
    }

    @Override
    public List<Mpa> getRatings() {
        String sqlRequest = "SELECT * FROM ratings";
        return jdbcTemplate.query(sqlRequest, new MpaRowMapper());
    }
}
