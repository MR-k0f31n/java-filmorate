package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAllFilm();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(int id);

    Film getFilmById(int id);

    List<Film> getTop10Film();

    void likeFilm();
}
