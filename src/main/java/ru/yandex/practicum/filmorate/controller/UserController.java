package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody NewUserRequest newUserRequest) {
        UserValidator.validate(newUserRequest);
        log.debug("Пользователь успешно добавлен в список пользователей; id={}", newUserRequest.getId());
        return userService.createUser(newUserRequest);
    }

    @PutMapping
    public UserDto update(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        if (updateUserRequest.getId() == null) {
            throw new NotFoundException("Необходимо указать id пользователя," +
                    " иначе его невозможно будет найти в нашей базе");
        }
        UserValidator.validate(updateUserRequest);
        log.debug("Пользователь с id={} успешно обновлён", updateUserRequest.getId());
        return userService.updateUser(updateUserRequest.getId(), updateUserRequest);
    }

    @GetMapping("/{userId}")
    public Optional<UserDto> getById(@PathVariable long userId) {
        log.trace("Поиск пользователя с id={}", userId);
        return Optional.ofNullable(userService.getById(userId));
    }

    // работа с друзьями
    @PutMapping("/{userId}/friends/{otherUserId}")
    public void addFriend(@PathVariable long userId, @PathVariable long otherUserId) {
        log.warn("Добавление в друзья пользователю id={} пользователя id={}", userId, otherUserId);
        userService.addFriend(userId, otherUserId);
    }

    @DeleteMapping("/{userId}/friends/{otherUserId}")
    public void removeFriend(@PathVariable long userId, @PathVariable long otherUserId) {
        log.warn("Удаление из друзей пользователя id={} пользователя id={}", userId, otherUserId);
        userService.removeFriend(userId, otherUserId);
    }

    @GetMapping("/{userId}/friends")
    public Collection<UserDto> getAllFriends(@PathVariable long userId) {
        log.warn("Поиск всех друзей пользователя id={}", userId);
        return userService.getUserFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherUserId}")
    public Collection<UserDto> getCommonFriends(@PathVariable long userId, @PathVariable long otherUserId) {
        log.warn("Поиск общий друзей пользователя id={} и пользователя id={}", userId, otherUserId);
        return userService.getCommonFriends(userId, otherUserId);
    }

}
