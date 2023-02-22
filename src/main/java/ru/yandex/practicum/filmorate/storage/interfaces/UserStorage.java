package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAllUser();

    User addUser(User user);

    User updateUser(User user);

    void deleteUser(int id);

    User findUser(User user);

    List<User> getUserFriend(User user);

    List<Film> findFilmUserLike(User user);
}
