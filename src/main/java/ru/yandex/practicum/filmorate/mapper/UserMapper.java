package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static User mapToUser(NewUserRequest newUserRequest) {
        User user = new User();
        user.setEmail(newUserRequest.getEmail());
        user.setLogin(newUserRequest.getLogin());
        user.setName(newUserRequest.getName());
        user.setBirthday(newUserRequest.getBirthday());
        return user;
    }

    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setLogin(user.getLogin());
        userDto.setName(user.getName());
        userDto.setBirthday(user.getBirthday());
        return userDto;
    }

    public static User updateUserFields(User user, UpdateUserRequest updateUserRequest) {
        if (updateUserRequest.hasEmail()) {
            user.setEmail(updateUserRequest.getEmail());
        }
        if (updateUserRequest.hasLogin()) {
            user.setLogin(updateUserRequest.getLogin());
        }
        if (updateUserRequest.hasName()) {
            user.setName(updateUserRequest.getName());
        }
        if (updateUserRequest.hasBirthday()) {
            user.setBirthday(updateUserRequest.getBirthday());
        }
        return user;
    }
}
