package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.FriendRequest;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

@Component
@RequiredArgsConstructor
@Qualifier(value = "friendDbStorage")
public class FriendDbStorage implements FriendStorage {
    private static final String SEND_FRIEND_REQUEST_QUERY = "insert into friend_requests(user_id_from, user_id_to) " +
            "values (?,?)";
    private static final String DELETE_FRIEND_REQUEST_QUERY = "delete from friend_requests where " +
            "user_id_from = ? and user_id_to = ?";
    private static final String NO_SUCH_FRIEND_REQUEST_MESSAGE = "Заявки в друзья между %s и %s не существует.";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public FriendRequest sendFriendRequest(Long userIdFrom, Long userIdTo) {
        if (!userExists(userIdFrom) || !userExists(userIdTo)) {
            throw new NotExistException("Один или оба пользователя не существуют.");
        }
        try {
            jdbcTemplate.update(SEND_FRIEND_REQUEST_QUERY, userIdFrom, userIdTo);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при отправке запроса на дружбу: " + e.getMessage(), e);
        }

        return new FriendRequest(userIdFrom, userIdTo, false);
    }

    @Override
    public FriendRequest deleteFriendRequest(Long userIdFrom, Long userIdTo) {
        if (!userExists(userIdFrom) || !userExists(userIdTo)) {
            throw new NotExistException("Один или оба пользователя не существуют.");
        }
        if (jdbcTemplate.update(DELETE_FRIEND_REQUEST_QUERY, userIdFrom, userIdTo) > 0)
            return new FriendRequest(userIdFrom, userIdTo, false);
        return null;

    }

    private boolean userExists(Long userId) {
        String checkUserQuery = "select count(*) from users where user_id = ?";
        return jdbcTemplate.queryForObject(checkUserQuery, new Object[]{userId}, Long.class) > 0;
    }

}
