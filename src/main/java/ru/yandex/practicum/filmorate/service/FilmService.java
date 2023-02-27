package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage storage;

    public List<Film> getTop10Film(int count) {
        log.trace("Попытка получить топ '{}' фильмов", count);
        return List.copyOf(filmStorage.getAllFilm().stream()
                .sorted((o1, o2) -> o2.getUserLike().size() - o1.getUserLike().size())
                .limit(count).collect(Collectors.toList()));
    }

    public void addLike (int filmId, int userId) {
        log.trace("Попытка поставить лайк фильму id '{}' пользователем id '{}'", filmId, userId);
        if (!storage.isIdContain(userId)) {
            throw new NotFoundException("Пользователь не обнаружен id " + userId);
        }
        filmStorage.getFilmById(filmId).getUserLike().add(userId);
    }

    public void removeLike (int filmId, int userId) {
        log.trace("Попытка удалить лайк фильму id '{}' пользователем id '{}'", filmId, userId);
        if (!filmStorage.getFilmById(filmId).getUserLike().contains(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не ставил лайк");
        }
        filmStorage.getFilmById(filmId).getUserLike().remove(userId);
    }

    public List<Film> getAllFilm() {
        log.trace("Попытка получить все фильмы");
        return filmStorage.getAllFilm();
    }

    public Film addFilm(Film film) {
        log.trace("Попытка добавить фильм name '{}'", film.getName());
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        log.trace("Попытка обновить фильм name '{}'", film.getName());
        return filmStorage.updateFilm(film);
    }

    public void deleteFilm(int id) {
        log.trace("Попытка удалить фильм id '{}'", id);
        filmStorage.deleteFilm(id);
    }

    public Film getFilmById(int id) {
        log.trace("Попытка получить фильм id '{}'", id);
        return filmStorage.getFilmById(id);
    }
}
