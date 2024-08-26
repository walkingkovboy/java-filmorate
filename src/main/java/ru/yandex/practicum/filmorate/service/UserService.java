package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserService extends BaseService<User> {

    Collection<User> getFriends(Long id);

    Collection<User> getCommonFriends(Long id, Long otherId);

}
