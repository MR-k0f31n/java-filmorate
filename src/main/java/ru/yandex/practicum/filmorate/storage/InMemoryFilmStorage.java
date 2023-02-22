package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private int currencyIdFilm = 1;
    private final Map<Integer, Film> films = new HashMap<>();
    @Override
    public List<Film> getAllFilm() {
        return List.copyOf(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(currencyIdFilm++);
        films.put(film.getId(), film);
        log.info("Получен запрос к эндпоинту: 'POST /films', фильм с name '{}' успешно записан, присвоен id '{}'",
                film.getName(), film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Не удалось найти фильм с id '{}'", film.getId());
            throw new NotFoundException("Фильма с таким id не существует");
        }
        films.put(film.getId(), film);
        log.info("Получен запрос к эндпоинту: 'PUT /films', фильм id name '{} {}' успешно Обновлен",
                film.getId(), film.getName());
        return film;
    }

    @Override
    public void deleteFilm(int id) {

    }

    @Override
    public Film getFilmById(int id) {
        return null;
    }

    @Override
    public List<Film> getTop10Film() {
        return null;
    }

    @Override
    public void likeFilm() {

    }
}
