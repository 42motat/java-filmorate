package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    // работа с друзьями
    @Override
    public void addFriend(long userId, long otherUserId) {
        User user = userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));
        User otherUser = userStorage.getById(otherUserId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));
        user.getFriends().add(otherUserId);
        otherUser.getFriends().add(userId);
    }

    @Override
    public void removeFriend(long userId, long otherUserId) {
        User user = userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));
        User otherUser = userStorage.getById(otherUserId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));
        user.getFriends().remove(otherUserId);
        otherUser.getFriends().remove(userId);
    }

    @Override
    public Collection<User> getUserFriends(long userId) {
        User user = userStorage.getById(userId).
                orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));
        Set<Long> friendsId = user.getFriends();
        return retrieveUsersFromUserId(friendsId);
    }

    @Override
    public Collection<User> getCommonFriends(long userId, long otherUserId) {
        User user = userStorage.getById(userId).
                orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));
        Collection<User> userFriends = retrieveUsersFromUserId(user.getFriends());

        User otherUser = userStorage.getById(otherUserId).
                orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));
        Collection<User> otherUserFriends = retrieveUsersFromUserId(otherUser.getFriends());

        return userFriends.stream()
                .filter(otherUserFriends::contains)
                .toList();
    }

    private Collection<User> retrieveUsersFromUserId(Collection<Long> userIds) {
        return userIds.stream()
                .map(userStorage::getById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}
