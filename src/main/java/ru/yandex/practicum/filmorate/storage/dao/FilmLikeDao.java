package ru.yandex.practicum.filmorate.storage.dao;

import java.util.Set;

public interface FilmLikeDao {
    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    Set<Long> userLikeByFilmId(Long filmId);
}
