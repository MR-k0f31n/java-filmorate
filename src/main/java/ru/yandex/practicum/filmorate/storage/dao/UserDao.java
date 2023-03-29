package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUser();

    User addUser(User user);

    User updateUser(User user);

    void deleteUser(Long id);

    User getUserById(Long id);
}
