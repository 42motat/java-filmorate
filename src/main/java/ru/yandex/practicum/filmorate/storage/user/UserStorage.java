package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Collection<User> getAllUsers();

    User create(User user);

    User update(User user);

    Optional<User> getById(long userId);

    void deleteById(long userId);

    void addFriend(long userId, long otherUserId);

    Collection<User> getUserFriends(long userId);

    Collection<User> getCommonFriends(long userId, long otherUserId);

    void removeFriend(long userId, long otherUserId);
}
