package ru.yandex.practicum.filmorate.model.mapper;

import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Film
                (
                        rs.getLong("film_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("release_date").toLocalDate(),
                        rs.getLong("duration"),
                        new HashSet<>(),
                        new HashSet<>(),
                        new Mpa(rs.getLong("mpa_id"), rs.getString("mpa_name"))
                );
    }
}
