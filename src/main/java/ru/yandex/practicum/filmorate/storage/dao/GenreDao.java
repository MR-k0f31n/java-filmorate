package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreDao {
    Genre getGenreById(Long id);

    List<Genre> getGenres();

    void addGenreFilm(Film film);

    void removeGenreFilm(Film film);
}
