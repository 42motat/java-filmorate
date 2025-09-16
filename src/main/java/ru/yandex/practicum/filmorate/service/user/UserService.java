package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserService {
    void addFriend(long userId, long otherUserId);
    void removeFriend(long userId, long otherUserId);
    Collection<User> getUserFriends(long userId);
    Collection<User> getCommonFriends(long userId, long otherUserId);
}
