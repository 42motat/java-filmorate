package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @GetMapping
    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        UserValidator.validate(user);
        log.debug("Пользователь успешно добавлен в список пользователей; id={}", user.getId());
        return userStorage.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (user.getId() == null) {
            throw new NotFoundException("Необходимо указать id пользователя," +
                    " иначе его невозможно будет найти в нашей базе");
        }
        UserValidator.validate(user);
        log.debug("Пользователь с id={} успешно обновлён", user.getId());
        return userStorage.update(user);
    }

    @GetMapping("/{userId}")
    public Optional<User> getById(@PathVariable long userId) {
        log.trace("Поиск пользователя с id={}", userId);
        return userStorage.getById(userId);
    }

    @DeleteMapping("/{userId}")
    public User deleteById(@PathVariable long userId) {
        log.trace("Удаление пользователя с id={}", userId);
        return userStorage.deleteById(userId);
    }

    // работа с друзьями
    @PutMapping("/{userId}/friends/{otherUserId}")
    public void addFriend(@PathVariable long userId, @PathVariable long otherUserId) {
        log.trace("Добавление в друзья пользователю id={} пользователя id={}", userId, otherUserId);
        userService.addFriend(userId, otherUserId);
    }

    @DeleteMapping("/{userId}/friends/{otherUserId}")
    public void removeFriend(@PathVariable long userId, @PathVariable long otherUserId) {
        log.trace("Удаление из друзей пользователя id={} пользователя id={}", userId, otherUserId);
        userService.removeFriend(userId, otherUserId);
    }

    @GetMapping("/{userId}/friends")
    public Collection<User> getAllFriends(@PathVariable long userId) {
        log.trace("Поиск всех друзей пользователя id={}", userId);
        return userService.getUserFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherUserId}")
    public Collection<User> getCommonFriends(@PathVariable long userId, @PathVariable long otherUserId) {
        log.trace("Поиск общий друзей пользователя id={} и пользователя id={}", userId, otherUserId);
        return userService.getCommonFriends(userId, otherUserId);
    }

}
