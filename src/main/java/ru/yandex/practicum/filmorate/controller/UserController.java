package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService service;

    @GetMapping
    public List<User> returnAllUser() {
        log.trace("Получен запрос к эндпоинту: 'GET /users'");
        return service.getAllUser();
    }

    @GetMapping("/{id}")
    public User getUserById (@PathVariable Long id) {
        log.trace("Получен запрос к эндпоинту: 'GET /users/id'");
        return service.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> viewFriend (@PathVariable Long id) {
        log.trace("Получен запрос к эндпоинту: 'GET /users/id/friends'");
        return service.viewUserFriend(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> commonFriends (@PathVariable Long id, @PathVariable Long otherId) {
        log.trace("Получен запрос к эндпоинту: 'GET /users/id/friends/common/otherId'");
        return service.commonFriend(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend (@PathVariable Long id, @PathVariable Long friendId) {
        log.trace("Получен запрос к эндпоинту: 'PUT /users/id/friends/friendId'");
        service.addFriend(id, friendId);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.trace("Получен запрос к эндпоинту: 'PUT /users'");
        return service.updateUser(user);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend (@PathVariable Long id, @PathVariable Long friendId) {
        log.trace("Получен запрос к эндпоинту: 'DELETE /users/id/friends/friendId'");
        service.removeFriend(id, friendId);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.trace("Получен запрос к эндпоинту: 'POST /users'");
        return service.addUser(user);
    }

    @DeleteMapping
    public void deleteUser (@PathVariable Long id) {
        log.trace("Получен запрос к эндпоинту: 'DELETE /users'");
        service.deleteUser(id);
    }
}
