package ru.yandex.practicum.filmorate.storage.dao.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.mapper.UserLikeRowMapper;
import ru.yandex.practicum.filmorate.storage.dao.FilmDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Primary
@Slf4j
@RequiredArgsConstructor
public class FilmStorageDB implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getAllFilm() {
        log.info("Запрошен список фильмов");
        String sqlRequest = "SELECT f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA, " +
                "m.MPA_ID, m.MPA_NAME, " +
                "fg.GENRE_ID, g.GENRE_NAME, fl.USER_LIKE " +
                "FROM FILMS f " +
                "LEFT JOIN MPA m  ON f.MPA = m.MPA_ID " +
                "LEFT JOIN FILM_GENRE fg ON f.FILM_ID = fg.FILM_ID " +
                "LEFT JOIN GENRES g  ON fg.GENRE_ID = g.GENRE_ID " +
                "LEFT JOIN FILM_LIKE fl ON F.film_id = fl.FILM_ID " +
                "WHERE f.film_id IN (SELECT film_id FROM films ORDER BY film_id ASC)";
        return jdbcTemplate.query(sqlRequest, listResultSetExtractor);
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
                    "LEFT JOIN mpa ON films.mpa = mpa.mpa_id " +
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
                "LEFT JOIN mpa ON films.mpa = mpa.mpa_id " +
                "WHERE film_name = ? ";
        return jdbcTemplate.queryForObject(sqlRequest, new FilmRowMapper(), film.getName());
    }

    @Override
    public Film updateFilm(Film film) {
        getFilmById(film.getId());
        String sqlRequest = "UPDATE FILMS " +
                "SET FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA = ? " +
                "WHERE FILM_ID = ?;";
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
            sqlRequest = "DELETE FROM FILM_GENRE WHERE FILM_ID = ? AND GENRE_ID IN " +
                    "(SELECT GENRE_ID  FROM FILM_GENRE fg WHERE FILM_ID = ?)";
            jdbcTemplate.update(sqlRequest, film.getId(), film.getId());
            for (Genre genre : film.getGenres()) {
                sqlRequest = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?);";
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
                    "LEFT JOIN mpa ON films.mpa = mpa.mpa_id " +
                    "WHERE film_id = ? ";
            Film film = jdbcTemplate.queryForObject(sqlRequest, new FilmRowMapper(), id);
            film.getGenres().addAll(getGenreFromFilm(id));
            film.getUserLike().addAll(getLikeFromFilm(id));
            log.info("Найден фильм name '{}' id '{}'", film.getName(), film.getId());
            return film;
        } catch (Throwable exception) {
            log.warn("Фильм с id = " + id + " не найден");
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
    }

    private final ResultSetExtractor<List<Film>> listResultSetExtractor = rs -> {
        Map<Long, Film> filmMap = new HashMap<>();
        Film film;
        while (rs.next()) {
            Long filmId = rs.getLong("FILM_ID");
            film = filmMap.get(filmId);
            if (film == null) {
                film = new Film(
                        rs.getLong("film_id"),
                        rs.getString("film_name"),
                        rs.getString("description"),
                        rs.getDate("release_date").toLocalDate(),
                        rs.getLong("duration"),
                        new Mpa(rs.getLong("mpa_id"), rs.getString("mpa_name"))
                );
                filmMap.put(filmId, film);
            }
            Set<Genre> genres = film.getGenres();
            if (rs.getLong("GENRE_ID") != 0) {
                genres.add(new Genre(rs.getLong("genre_id"), rs.getString("genre_name")));
            }
            Set<Long> userLike = film.getUserLike();
            if (rs.getLong("USER_LIKE") != 0) {
                userLike.add(rs.getLong("USER_LIKE"));
            }
        }
        return new ArrayList<>(filmMap.values());
    };

    private Set<Genre> getGenreFromFilm(Long id) {
        String sqlRequest = "SELECT genres.genre_id, genre_name FROM film_genre " +
                "LEFT JOIN genres ON film_genre.genre_id = genres.genre_id " +
                "WHERE film_id = ?";
        return new HashSet<>(jdbcTemplate.query(sqlRequest, new GenreRowMapper(), id));
    }

    private Set<Long> getLikeFromFilm(Long id) {
        String sqlRequest = "SELECT user_like FROM film_like " +
                "WHERE film_id = ?";
        return new HashSet<>(jdbcTemplate.query(sqlRequest, new UserLikeRowMapper(), id));
    }
}
