package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Qualifier("UserDbRepository")
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto createUser(NewUserRequest newUserRequest) {
        User user = UserMapper.mapToUser(newUserRequest);
        user = userRepository.create(user);
        return UserMapper.mapToUserDto(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getById(long userId) {
        return userRepository.getById(userId)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь id=" + userId + " не найден"));
    }

    public UserDto updateUser(long userId, UpdateUserRequest updateUserRequest) {
        User updatedUser = userRepository.getById(userId)
                .map(user -> UserMapper.updateUserFields(user, updateUserRequest))
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        updatedUser = userRepository.update(updatedUser);
        return UserMapper.mapToUserDto(updatedUser);
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

    // работа с друзьями
    @Override
    public void addFriend(long userId, long otherUserId) {
        User user = userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));
        User otherUser = userRepository.getById(otherUserId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));
//        user.getFriends().add(otherUserId);
        userRepository.addFriend(userId, otherUserId);

    }

    @Override
    public void removeFriend(long userId, long otherUserId) {
        User user = userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));
        User otherUser = userRepository.getById(otherUserId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));
        userRepository.removeFriend(userId, otherUserId);
        System.out.println(userRepository.getUserFriends(userId));
    }

    @Override
    public Collection<UserDto> getUserFriends(long userId) {
        User user = userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));
        Set<Long> friendsId = user.getFriends();
        userRepository.getUserFriends(user.getId());
        return userRepository.getUserFriends(userId)
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public Collection<UserDto> getCommonFriends(long userId, long otherUserId) {
        User user = userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));
        User otherUser = userRepository.getById(otherUserId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя нет в базе данных"));
        return userRepository.getCommonFriends(userId, otherUserId)
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }
}
