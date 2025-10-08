package ru.yandex.practicum.filmorate.repository.user;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.BaseDbRepository;

import java.util.Collection;
import java.util.Optional;

@Repository
@Primary
public class UserDbRepository extends BaseDbRepository<User> implements UserRepository {

    // шаблоны запросов
    private static final String GET_ALL_QUERY = """
            SELECT * FROM users
            """;

    private static final String GET_BY_ID_QUERY = """
            SELECT * FROM users WHERE userId = ?
            """;

    private static final String CREATE_QUERY = """
            INSERT INTO users(email, login, name, birthday) VALUES (?, ?, ?, ?)
            """;

    private static final String UPDATE_QUERY = """
            UPDATE users SET email = ?, login = ?, name = ?, birthday = ?
            WHERE userId = ?
            """;

    private static final String DELETE_QUERY = """
            DELETE FROM users WHERE userId = ?
            """;

    // шаблоны для работы с друзьями
    private static final String ADD_FRIEND_QUERY = """
            INSERT INTO friends (userId, otherUserId) VALUES (?, ?)
            """;

    private static final String GET_FRIENDS_QUERY = """
            SELECT * FROM users u
            JOIN friends f ON u.userId = f.otherUserId
            WHERE f.userId = ?
            """;

    private static final String GET_COMMON_FRIENDS_QUERY = """
            SELECT * FROM users u
            JOIN friends f1 ON u.userId = f1.otherUserId
            JOIN friends f2 ON u.userId = f2.otherUserId
            WHERE f1.userId = ? AND f2.userId = ?
            """;

    private static final String REMOVE_FRIEND_QUERY = """
            DELETE FROM friends WHERE userId = ? AND otherUserId = ?
            """;

    public UserDbRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    // базовые методы
    @Override
    public Collection<User> getAllUsers() {
        return getAll(GET_ALL_QUERY);
    }

    @Override
    public User create(User user) {
        long userId = insert(
                CREATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(userId);
        return user;
    }

    @Override
    public User update(User user) {
        update(UPDATE_QUERY, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public Optional<User> getById(long userId) {
        return getOne(GET_BY_ID_QUERY, userId);
    }

    @Override
    public void deleteById(long userId) {
        delete(DELETE_QUERY, userId);
    }

    // друзья
    @Override
    public void addFriend(long userId, long otherUserId) {
        update(ADD_FRIEND_QUERY, userId, otherUserId);
    }

    @Override
    public Collection<User> getUserFriends(long userId) {
        return getAll(GET_FRIENDS_QUERY, userId);
    }

    @Override
    public Collection<User> getCommonFriends(long userId, long otherUserId) {
        return getAll(GET_COMMON_FRIENDS_QUERY, userId, otherUserId);
    }

    @Override
    public void removeFriend(long userId, long otherUserId) {
        delete(REMOVE_FRIEND_QUERY, userId, otherUserId);
    }
}
