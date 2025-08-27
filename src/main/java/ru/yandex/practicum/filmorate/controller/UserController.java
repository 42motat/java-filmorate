package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        UserValidator.validate(user);
        user.setId(getNextId());

        users.put(user.getId(), user);

        log.debug("Пользователь успешно добавлен в список пользователей; id={}", user.getId());

        return user;
    }

    private long getNextId() {
        long currentMaxId = users.keySet().stream()
                .mapToLong(userId -> userId)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        if (user.getId() == null) {
            throw new UserNotFoundException("Необходимо указать id пользователя," +
                    " иначе его невозможно будет найти в нашей базе");
        }

        UserValidator.validate(user);

        if (users.containsKey(user.getId())) {
            User oldUser = users.get(user.getId());
            // валидация проходит раньше поиска, поэтому можно считать все переданные значения валидными
            oldUser.setEmail(user.getEmail());
            oldUser.setLogin(user.getLogin());
            oldUser.setName(user.getName());
            oldUser.setBirthday(user.getBirthday());

            log.debug("Пользователь с id={} успешно обновлён", user.getId());

            return oldUser;
        }
        throw new UserNotFoundException("Указанный пользователь не найден");
    }

}
