package ru.yandex.practicum.filmorate.storage.inMemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dao.FilmDao;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Component
@Slf4j
public class InMemoryFilmDao implements FilmDao {

    private long currencyIdFilm = 1;
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public List<Film> getAllFilm() {
        if (films.size() == 0) {
            log.info("Запрошен список фильмов, список пуст");
        }
        log.info("Запрошен список фильмов, кол-во фильмов '{}'", films.size());
        return List.copyOf(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(currencyIdFilm++);
        films.put(film.getId(), film);
        log.info("Фильм с name '{}' успешно записан, присвоен id '{}'",
                film.getName(), film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!checkFilm(film.getId())) {
            throw new NotFoundException("Фильм не найден id " + film.getId());
        }
        films.put(film.getId(), film);
        log.info("Фильм id name '{} {}' успешно Обновлен",
                film.getId(), film.getName());
        return film;
    }

    @Override
    public void deleteFilm(Long id) {
        if (!checkFilm(id)) {
            throw new NotFoundException("Фильм не найден id " + id);
        }
        log.info("Фильм id '{}' name '{}' удален", id, films.get(id).getName());
        films.remove(id);
    }

    @Override
    public Film getFilmById(Long id) {
        if (!checkFilm(id)) {
            throw new NotFoundException("Фильм не найден id " + id);
        }
        return films.get(id);
    }

    private boolean checkFilm(Long id) {
        return films.containsKey(id);
    }
}
