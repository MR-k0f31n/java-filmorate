package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.dao.implement.GenreStorageDB;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class GenreService {
    private final GenreStorageDB genreStorageDB;

    public Genre getGenreById(Long id) {
        return genreStorageDB.getGenreById(id);
    }

    public List<Genre> getGenres() {
        return genreStorageDB.getGenres();
    }
}
