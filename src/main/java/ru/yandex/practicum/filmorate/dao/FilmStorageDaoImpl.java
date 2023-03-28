package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.HashSet;
import java.util.List;

@Component
@Primary
@Slf4j
@RequiredArgsConstructor
public class FilmStorageDaoImpl implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getAllFilm() {
        return null;
    }

    @Override
    public Film addFilm(Film film) {
        String sql = "INSERT INTO films (title, description, release_date, duration, genre, user_like, rating) " +
                "values (?, ?, ?, ?, ?, ?, ?)";
        return null;
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public void deleteFilm(Long id) {

    }

    @Override
    public Film getFilmById(Long id) {
        SqlRowSet filmRow = jdbcTemplate.queryForRowSet("SELECT * FROM film WHERE film_id = ?", id);

        if (!filmRow.next()) {
            log.info("Фильм id '{}' не найден",id);
            throw new NotFoundException("Фильм не найден id " + id);
        }
        return makeFilm(filmRow);
    }

    @Override
    public boolean checkFilm(Long id) {
        SqlRowSet filmRow = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE film_id = ?", id);
        if (!filmRow.next()) {
            return true;
        }
        log.debug("Фильм с id {} не найден", id);
        throw new NotFoundException("Фильм не найден id " + id);
    }

    private Film makeFilm(SqlRowSet sql) {
        return new Film(
                sql.getLong("film_id"),
                sql.getString("title"),
                sql.getString("description"),
                sql.getDate("release_date").toLocalDate(),
                sql.getLong("duration"),
                new HashSet<>(),
                new HashSet<>(),
                sql.getLong("rating")
                );
    }
}
