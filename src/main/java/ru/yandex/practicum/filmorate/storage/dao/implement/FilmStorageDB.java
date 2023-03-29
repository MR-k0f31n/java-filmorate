package ru.yandex.practicum.filmorate.storage.dao.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.dao.FilmDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Component
@Primary
@Slf4j
@RequiredArgsConstructor
public class FilmStorageDB implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getAllFilm() {
        String sqlRequest = "SELECT * FROM films JOIN mpa ON films.mpa = mpa.mpa_id";
        return jdbcTemplate.query(sqlRequest, new FilmRowMapper());
    }

    @Override
    public Film addFilm(Film film) {
        String sqlRequest = "INSERT INTO films (name, description, release_date, duration, genre, mpa) " +
                "values (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update
                (
                        sqlRequest,
                        film.getName(),
                        film.getDuration(),
                        film.getReleaseDate(),
                        film.getDuration(),
                        film.getGenre(),
                        film.getMpa().getId()
                );
        //sqlRequest = "SELECT * FROM films WHERE name = ?";
        /*sqlRequest = "SELECT * " +
                "FROM films " +
                "JOIN mpa ON films.mpa = mpa.mpa_id " +
                "WHERE film_id = ? ";
        return jdbcTemplate.queryForObject(sqlRequest, new FilmRowMapper(), film.getName());*/
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        getFilmById(film.getId());
        String sqlRequest = "UPDATE films " +
                "SET title = ?, description = ?, release_date = ?, duration = ?, genre = ?, rating = ?" +
                "WHERE user_id = ?";
        jdbcTemplate.update
                (
                        sqlRequest,
                        film.getName(),
                        film.getDescription(),
                        film.getReleaseDate(),
                        film.getDuration(),
                        film.getGenre(),
                        film.getMpa().getId()
                );
        return getFilmById(film.getId());
    }

    @Override
    public void deleteFilm(Long id) {
        getFilmById(id);
        String sqlRequest = "DELETE FROM films WHERE film_id = ?";
        jdbcTemplate.update(sqlRequest, id);
    }

    @Override
    public Film getFilmById(Long id) {
        try {
            String sqlRequest = "SELECT * " +
                    "FROM films " +
                    "JOIN ratings ON films.rating = ratings.rating_id " +
                    "WHERE film_id = ? ";
            return jdbcTemplate.queryForObject(sqlRequest, new FilmRowMapper(), id);
        } catch (Throwable exception) {
            log.warn("Фильм с id = " + id + " не найден");
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
    }
}
