package ru.yandex.practicum.filmorate.storage.dao;

import java.util.Set;

public interface FriendsDao {
    void addFriend (Long userId, Long friendId);
    void removeFriend (Long userId, Long friendId);
    Set<Long> getAllFriendUserById (Long userId);
}
