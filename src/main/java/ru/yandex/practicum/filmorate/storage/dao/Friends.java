package ru.yandex.practicum.filmorate.storage.dao;

import java.util.HashSet;
import java.util.List;

public interface Friends {
    void addFriend (Long userId, Long friendId);
    void removeFriend (Long userId, Long friendId);
    HashSet<Long> getAllFriendUserById (Long userId);
    List<Boolean> checkStatus(Long userId, Long friendId);
}
