package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
@Qualifier(value = "userDbStorage")
public class UserDbStorage implements UserStorage {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String GET_FRIENDS_QUERY = "select user_id, email, name, login, birthday " +
            "from users u " +
            "inner join friend_requests fr on u.user_id = fr.user_id_to " +
            "where fr.user_id_from = ?";

    private static final String GET_COMMON_FRIENDS = "select u.user_id, email, name, login, birthday " +
            "from users u " +
            "inner join friend_requests fr on u.user_id = fr.user_id_to " +
            "where fr.user_id_from = ? " +
            "intersect " +
            "select u.user_id, email, name, login, birthday " +
            "from users u " +
            "inner join friend_requests fr on u.user_id = fr.user_id_to " +
            "where fr.user_id_from = ?";
    private static final String GET_USER_QUERY = "select * from users where user_id = ?";
    private static final String INSERT_USER_QUERY = "insert into users(login, name, email, birthday) " +
            "values (?, ?, ?, ?)";
    private static final String DELETE_USER_QUERY = "delete from users where user_id = ?";
    private static final String UPDATE_USER_QUERY = "update users set email = ?, login = ?, name = ?, birthday = ? " +
            "where user_id = ?;";
    private static final String GET_ALL_USERS_QUERY = "select * from users";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> getFriends(Long id) {
        return jdbcTemplate.query(GET_FRIENDS_QUERY, (rs, rowNum) -> makeUser(rs), id);
    }

    @Override
    public Collection<User> getCommonFriends(Long id, Long otherId) {
        return jdbcTemplate.query(GET_COMMON_FRIENDS, (rs, rowNum) -> makeUser(rs), id, otherId);
    }

    @Override
    public User get(Long id) {
        List<User> users = jdbcTemplate.query(GET_USER_QUERY, (rs, rowNum) -> makeUser(rs), id);
        if (!users.isEmpty())
            return users.stream().findFirst().get();
        throw new NotExistException(String.format("Пользователя с id %s не существует", id));
    }

    @Override
    public User add(User user) {
        String birthday = user.getBirthday().format(DATE_FORMATTER);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT_USER_QUERY, new String[]{"user_id"});
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getName());
            statement.setString(3, user.getEmail());
            statement.setString(4, birthday);
            return statement;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User remove(User user) {
        if (jdbcTemplate.update(DELETE_USER_QUERY, user.getId()) > 0)
            return user;
        throw new NotExistException(String.format("Пользователя с id %s не существует", user.getId()));
    }

    @Override
    public User update(User user) {
        if (jdbcTemplate.update(UPDATE_USER_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday().format(DATE_FORMATTER),
                user.getId()) > 0)
            return user;
        throw new NotExistException(String.format("Пользователя с id %s не существует", user.getId()));
    }

    @Override
    public Boolean contains(Long id) {
        return null;
    }

    @Override
    public Collection<User> getAll() {
        return jdbcTemplate.query(GET_ALL_USERS_QUERY, (rs, rowNum) -> makeUser(rs));
    }

    private User makeUser(ResultSet rs) throws SQLException {
        LocalDate birthday = LocalDate.parse(rs.getString("birthday"), DATE_FORMATTER);
        return new User(rs.getLong("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                birthday);
    }
}

