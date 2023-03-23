package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAllFilm();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Long id);

    Film getFilmById(Long id);

    boolean checkFilm (Long id);
}
