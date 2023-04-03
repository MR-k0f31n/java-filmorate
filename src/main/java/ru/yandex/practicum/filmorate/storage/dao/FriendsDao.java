package ru.yandex.practicum.filmorate.storage.dao;

public interface FriendsDao {
    void addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);
}
