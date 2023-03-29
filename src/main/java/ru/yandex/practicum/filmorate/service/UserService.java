package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserDao userDao;

    public void addFriend(Long userId, Long friendID) {
        log.trace("Попытка добавить в друзья");
        userDao.getUserById(userId).getFriends().add(friendID);
        log.info("Пользователи id '{}' добавил пользователя id '{}' друзья", userId, friendID);
    }

    public void removeFriend(Long userId, Long friendID) {
        log.trace("Попытка удалить из друзей");
        userDao.getUserById(userId).getFriends().remove(friendID);
        userDao.getUserById(friendID).getFriends().remove(userId);
        log.info("Пользователи id '{}' и id '{}' не друзья", userId, friendID);
    }

    public List<User> commonFriend(Long userId, Long otherId) {
        log.trace("Попытка получить список общих друзей");
        User otherUser = getUserById(otherId);
        Set<Long> commonFriendsId = userDao.getUserById(userId).getFriends()
                .stream().filter(otherUser.getFriends()::contains)
                .collect(Collectors.toSet());
        return getAllUser().stream().filter(user -> commonFriendsId.contains(user.getId()))
                .collect(Collectors.toList());
    }

    public List<User> getAllUser() {
        log.trace("Попытка получить список всех пользователей");
        return userDao.getAllUser();
    }

    public User addUser(User user) {
        log.trace("Попытка добавить пользователя");
        return userDao.addUser(user);
    }

    public User updateUser(User user) {
        log.trace("Попытка обновить пользователя");
        return userDao.updateUser(user);
    }

    public void deleteUser(Long id) {
        log.trace("Попытка удалить пользователя");
        userDao.deleteUser(id);
    }

    public List<User> viewUserFriend(Long userId) {
        userDao.getUserById(userId);
        List<User> list = new ArrayList<>();
        userDao.getUserById(userId).getFriends().forEach(id -> {
            list.add(userDao.getUserById(id));
        });
        log.trace("Попытка посмотреть список друзей пользователя id '{}'", userId);
        return list;
    }

    public User getUserById(Long id) {
        log.trace("Попытка найти пользователя id '{}'", id);
        return userDao.getUserById(id);
    }
}
