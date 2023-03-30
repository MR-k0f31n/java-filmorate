package ru.yandex.practicum.filmorate.storage.dao.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

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
        String sqlRequest = "SELECT * FROM genres";
        return jdbcTemplate.query(sqlRequest, new GenreRowMapper());
    }

    @Override
    public void addGenreFilm(Film film) {
        String sqlRequest = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
        Long idFilm = film.getId();
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(sqlRequest, idFilm, genre.getId());
        }

    }

    @Override
    public void removeGenreFilm(Film film) {
        String sqlRequest = "DELETE FROM film_genre WHERE film_id = ? AND genre_id = ? ";
        Long idFilm = film.getId();
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(sqlRequest, idFilm, genre.getId());
        }
    }
}
