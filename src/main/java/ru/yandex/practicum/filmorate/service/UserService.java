package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public void addFriend (Long userId, Long friendID) {
        log.trace("Попытка добавить в друзья");
        if (!userStorage.checkUser(userId)) {
            throw new NotFoundException("Пользователь не обнаружен id " + userId);
        }
        if (!userStorage.checkUser(friendID)) {
            throw new NotFoundException("Пользователь не обнаружен id " + friendID);
        }
        userStorage.getUserById(userId).getFriends().add(friendID);
        userStorage.getUserById(friendID).getFriends().add(userId);
        log.info("Пользователи id '{}' и id '{}' друзья",userId, friendID);
    }

    public void removeFriend (Long userId, Long friendID) {
        log.trace("Попытка удалить из друзей");
        if (!userStorage.checkUser(userId)) {
            throw new NotFoundException("Пользователь не обнаружен id " + userId);
        }
        if (!userStorage.checkUser(friendID)) {
            throw new NotFoundException("Пользователь не обнаружен id " + friendID);
        }
        if (!userStorage.getUserById(userId).getFriends().contains(friendID)
                || !userStorage.getUserById(friendID).getFriends().contains(userId)) {
            throw new IncorrectParameterException(String.format("Пользователи id \"%s\" и id \"%s\" еще не друзья."
                    , userId, friendID));
        }
        userStorage.getUserById(userId).getFriends().remove(friendID);
        userStorage.getUserById(friendID).getFriends().remove(userId);
        log.info("Пользователи id '{}' и id '{}' не друзья",userId, friendID);
    }

    public List<User> commonFriend (Long userId, Long otherId) {
        log.trace("Попытка получить список общих друзей");
        User otherUser = getUserById(otherId);
        Set<Long> commonFriendsId = userStorage.getUserById(userId).getFriends()
                .stream().filter(otherUser.getFriends()::contains)
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

    public void deleteUser(Long id) {
        log.trace("Попытка удалить пользователя");
        userStorage.deleteUser(id);
    }

    public List<User> viewUserFriend(Long userId) {
        if (!userStorage.checkUser(userId)) {
            throw new NotFoundException("Пользователь не обнаружен id " + userId);
        }
        List<User> list = new ArrayList<>();
        userStorage.getUserById(userId).getFriends().forEach(id -> {
            list.add(userStorage.getUserById(id));
        });
        log.trace("Попытка посмотреть список друзей пользователя id '{}'", userId);
        return list;
    }

    public User getUserById(Long id) {
        log.trace("Попытка найти пользователя id '{}'", id);
        return userStorage.getUserById(id);
    }
}
