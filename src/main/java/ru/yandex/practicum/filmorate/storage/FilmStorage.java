package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAllFilm();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Integer id);

    Film getFilmById(Integer id);

    boolean checkFilm (Integer id);
}
