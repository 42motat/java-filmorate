package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto createUser(NewUserRequest newUserRequest);

    Collection<UserDto> getAllUsers();

    UserDto getById(long userId);

    UserDto updateUser(long userId, UpdateUserRequest updateUserRequest);

    void deleteUser(long userId);

    void addFriend(long userId, long otherUserId);

    void removeFriend(long userId, long otherUserId);

    Collection<UserDto> getUserFriends(long userId);

    Collection<UserDto> getCommonFriends(long userId, long otherUserId);
}
