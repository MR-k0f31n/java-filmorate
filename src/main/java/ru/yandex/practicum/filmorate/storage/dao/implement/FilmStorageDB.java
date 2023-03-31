package ru.yandex.practicum.filmorate.storage.dao.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.mapper.UserLikeRowMapper;
import ru.yandex.practicum.filmorate.storage.dao.FilmDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Primary
@Slf4j
@RequiredArgsConstructor
public class FilmStorageDB implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getAllFilm() {
        log.info("Запрошен список фильмов");
        String sqlRequest = "SELECT * FROM films JOIN mpa ON films.mpa = mpa.mpa_id";
        List<Film> list = jdbcTemplate.query(sqlRequest, new FilmRowMapper());
        list.forEach(id -> {
            id.getUserLike().addAll(getLikeFromFilm(id.getId()));
            id.getGenres().addAll(getGenreFromFilm(id.getId()));
        });
        return list;
    }

    @Override
    public Film addFilm(Film film) {
        String sqlRequest = "INSERT INTO films (film_name, description, release_date, duration, mpa) " +
                "values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(
                sqlRequest,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        if (film.getGenres() != null) {
            sqlRequest = "SELECT * " +
                    "FROM films " +
                    "JOIN mpa ON films.mpa = mpa.mpa_id " +
                    "WHERE film_name = ? ";
            Long filmId = jdbcTemplate.queryForObject(sqlRequest, new FilmRowMapper(), film.getName()).getId();
            sqlRequest = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlRequest, filmId, genre.getId());
            }
            log.info("Фильм с name '{}' успешно записан, присвоен id '{}'", film.getName(), filmId);
            return getFilmById(filmId);
        }
        sqlRequest = "SELECT * " +
                "FROM films " +
                "JOIN mpa ON films.mpa = mpa.mpa_id " +
                "WHERE film_name = ? ";
        return jdbcTemplate.queryForObject(sqlRequest, new FilmRowMapper(), film.getName());
    }

    @Override
    public Film updateFilm(Film film) {
        getFilmById(film.getId());
        String sqlRequest = "UPDATE films " +
                "SET film_name = ?, description = ?, release_date = ?, duration = ?, mpa = ? " +
                "WHERE film_id = ?";
        jdbcTemplate.update(
                sqlRequest,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        if (film.getGenres() != null) {
            sqlRequest = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?;";
            jdbcTemplate.update(sqlRequest, film.getId());
            sqlRequest = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?);";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlRequest, film.getId(), genre.getId());
            }
        }
        log.info("Фильм с name '{}' успешно записан, id '{}'", film.getName(), film.getId());
        return getFilmById(film.getId());
    }

    @Override
    public void deleteFilm(Long id) {
        getFilmById(id);
        String sqlRequest = "DELETE FROM films WHERE film_id = ?";
        jdbcTemplate.update(sqlRequest, id);
        log.info("Фильм id '{}' удален", id);
    }

    @Override
    public Film getFilmById(Long id) {
        try {
            String sqlRequest = "SELECT * FROM films " +
                    "JOIN mpa ON films.mpa = mpa.mpa_id " +
                    "WHERE film_id = ? ";
            Film film = jdbcTemplate.queryForObject(sqlRequest, new FilmRowMapper(), id);
            film.getGenres().addAll(getGenreFromFilm(id));
            film.getUserLike().addAll(getLikeFromFilm(id));
            log.info("Найден фильм name '{}' id '{}'",film.getName(), film.getId());
            return film;
        } catch (Throwable exception) {
            log.warn("Фильм с id = " + id + " не найден");
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
    }

    private Set<Genre> getGenreFromFilm(Long id) {
        String sqlRequest = "SELECT genres.genre_id, genre_name FROM film_genre " +
                "JOIN genres ON film_genre.genre_id = genres.genre_id " +
                "WHERE film_id = ?";
        return new HashSet<>(jdbcTemplate.query(sqlRequest, new GenreRowMapper(), id));
    }

    private Set<Long> getLikeFromFilm(Long id) {
        String sqlRequest = "SELECT user_like FROM film_like " +
                "WHERE film_id = ?";
        return new HashSet<>(jdbcTemplate.query(sqlRequest, new UserLikeRowMapper(), id));
    }
}
