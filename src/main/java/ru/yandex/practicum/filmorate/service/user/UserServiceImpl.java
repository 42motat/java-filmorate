package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Qualifier("UserDbRepository")
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public UserDto createUser(NewUserRequest newUserRequest) {
        User user = UserMapper.mapToUser(newUserRequest);
        user = userStorage.create(user);
        return UserMapper.mapToUserDto(user);
    }

    public List<UserDto> getAllUsers() {
        return userStorage.getAllUsers()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getById(long userId) {
        return userStorage.getById(userId)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь id=" + userId + " не найден"));
    }

    public UserDto updateUser(long userId, UpdateUserRequest updateUserRequest) {
        User updatedUser = userStorage.getById(userId)
                .map(user -> UserMapper.updateUserFields(user, updateUserRequest))
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        updatedUser = userStorage.update(updatedUser);
        return UserMapper.mapToUserDto(updatedUser);
    }

    @Override
    public void deleteUser(long userId) {
        userStorage.deleteById(userId);
    }

    // работа с друзьями
    @Override
    public void addFriend(long userId, long otherUserId) {
        User user = userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));
        User otherUser = userStorage.getById(otherUserId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));
//        user.getFriends().add(otherUserId);
        userStorage.addFriend(userId, otherUserId);

    }

    @Override
    public void removeFriend(long userId, long otherUserId) {
        User user = userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));
        User otherUser = userStorage.getById(otherUserId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));
        userStorage.removeFriend(userId, otherUserId);
        System.out.println(userStorage.getUserFriends(userId));
    }

    @Override
    public Collection<UserDto> getUserFriends(long userId) {
        User user = userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));
        Set<Long> friendsId = user.getFriends();
        userStorage.getUserFriends(user.getId());
        return userStorage.getUserFriends(userId)
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public Collection<UserDto> getCommonFriends(long userId, long otherUserId) {
        User user = userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));
        User otherUser = userStorage.getById(otherUserId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));
        return userStorage.getCommonFriends(userId, otherUserId)
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }
}
