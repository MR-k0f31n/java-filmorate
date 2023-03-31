package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dao.FilmDao;
import ru.yandex.practicum.filmorate.storage.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.storage.dao.UserDao;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmDao filmDao;
    private final UserDao userDao;
    private final FilmLikeDao filmLikeDao;

    public List<Film> getTop10Film(Long count) {
        log.trace("Попытка получить топ '{}' фильмов", count);
        return List.copyOf(getAllFilm().stream()
                .sorted((o1, o2) -> o2.getUserLike().size() - o1.getUserLike().size())
                .limit(count).collect(Collectors.toList()));
    }

    public void addLike(Long filmId, Long userId) {
        log.trace("Попытка поставить лайк фильму id '{}' пользователем id '{}'", filmId, userId);
        userDao.getUserById(userId);
        filmDao.getFilmById(filmId);
        filmLikeDao.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        log.trace("Попытка удалить лайк фильму id '{}' пользователем id '{}'", filmId, userId);
        filmLikeDao.removeLike(filmId, userId);
    }

    public List<Film> getAllFilm() {
        log.trace("Попытка получить все фильмы");
        return filmDao.getAllFilm();
    }

    public Film addFilm(Film film) {
        log.trace("Попытка добавить фильм name '{}'", film.getName());
        return filmDao.addFilm(film);
    }

    public Film updateFilm(Film film) {
        log.trace("Попытка обновить фильм name '{}'", film.getName());
        return filmDao.updateFilm(film);
    }

    public void deleteFilm(Long id) {
        log.trace("Попытка удалить фильм id '{}'", id);
        filmDao.deleteFilm(id);
    }

    public Film getFilmById(Long id) {
        log.trace("Попытка получить фильм id '{}'", id);
        return filmDao.getFilmById(id);
    }
}
