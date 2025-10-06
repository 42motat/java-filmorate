package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User create(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (users.containsKey(user.getId())) {
            User oldUser = users.get(user.getId());
            // валидация проходит раньше поиска, поэтому можно считать все переданные значения валидными
            oldUser.setEmail(user.getEmail());
            oldUser.setLogin(user.getLogin());
            oldUser.setName(user.getName());
            oldUser.setBirthday(user.getBirthday());

            return oldUser;
        }
        throw new NotFoundException("Указанный пользователь не найден");
    }

    @Override
    public Optional<User> getById(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public void deleteById(long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Проверьте корректность введённого id пользователя");
        }
        users.remove(userId);
    }

    @Override
    public void addFriend(long userId, long otherUserId) {
        // заполнитель
    }

    @Override
    public Collection<User> getUserFriends(long userId) {
        return List.of();
    }

    @Override
    public Collection<User> getCommonFriends(long userId, long otherUserId) {
        return List.of();
    }

    @Override
    public void removeFriend(long userId, long otherUserId) {
        // заполнитель
    }

    private long getNextId() {
        long currentMaxId = users.keySet().stream()
                .mapToLong(userId -> userId)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
