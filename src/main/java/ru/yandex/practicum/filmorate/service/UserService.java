package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend (int userId, int friendID) {
        log.trace("Попытка добавить в друзья");
        if (!userStorage.isIdContain(userId)) {
            throw new NotFoundException("Пользователь не обнаружен id " + userId);
        }
        if (!userStorage.isIdContain(friendID)) {
            throw new NotFoundException("Пользователь не обнаружен id " + friendID);
        }
        userStorage.getUserById(userId).getFriends().add(friendID);
        userStorage.getUserById(friendID).getFriends().add(userId);
        log.info("Пользователи id '{}' и id '{}' друзья",userId, friendID);
    }

    public void removeFriend (int userId, int friendID) {
        log.trace("Попытка удалить из друзей");
        if (!userStorage.getUserById(userId).getFriends().contains(friendID)
                || !userStorage.getUserById(friendID).getFriends().contains(userId)) {
            throw new IncorrectParameterException(String.format("Пользователи id \"%s\" и id \"%s\" еще не друзья."
                    , userId, friendID));
        }
        if (!userStorage.isIdContain(userId)) {
            throw new NotFoundException("Пользователь не обнаружен id " + userId);
        }
        if (!userStorage.isIdContain(friendID)) {
            throw new NotFoundException("Пользователь не обнаружен id " + friendID);
        }
        userStorage.getUserById(userId).getFriends().remove(friendID);
        userStorage.getUserById(friendID).getFriends().remove(userId);
        log.info("Пользователи id '{}' и id '{}' не друзья",userId, friendID);
    }

    public List<User> commonFriend (int userId, int otherId) {
        log.trace("Попытка получить список общих друзей");
        Set<Integer> commonFriendsId = userStorage.getUserById(userId).getFriends()
                .stream().filter(userStorage.getUserById(otherId).getFriends()::contains)
                .collect(Collectors.toSet());
        return getAllUser().stream().filter(user -> commonFriendsId.contains(user.getId()))
                .collect(Collectors.toList());
    }

    public List<User> getAllUser() {
        log.trace("Попытка получить список всех пользователей");
        return userStorage.getAllUser();
    }

    public User addUser (User user) {
        log.trace("Попытка добавить пользователя");
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        log.trace("Попытка обновить пользователя");
        return userStorage.updateUser(user);
    }

    public void deleteUser(int id) {
        log.trace("Попытка удалить пользователя");
        userStorage.deleteUser(id);
    }

    public List<User> viewUserFriend(int userId) {
        log.trace("Попытка посмотреть список друзей пользователя id '{}'", userId);
        return userStorage.viewUserFriend(userId);
    }

    public User getUserById(int id) {
        log.trace("Попытка найти пользователя id '{}'", id);
        return userStorage.getUserById(id);
    }
}
