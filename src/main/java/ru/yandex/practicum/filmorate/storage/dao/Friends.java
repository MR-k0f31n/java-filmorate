package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface Friends {
    void addFriend (Long userId, Long friendId);
    void removeFriend (Long userId, Long friendId);
    List<User> getAllFriendUserById (Long userId);
    List<Boolean> checkStatus(Long userId, Long friendId);
}
