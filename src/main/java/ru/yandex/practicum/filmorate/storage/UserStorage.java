package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAllUser();

    User addUser(User user);

    User updateUser(User user);

    void deleteUser(int id);

    List<User> viewUserFriend(int userId);

    User getUserById(int id);

    boolean isIdContain(int id);
}
