package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    @Qualifier(value = "userDbStorage")
    private UserStorage userStorage;

    @Override
    public Collection<User> getFriends(Long id) {
        return userStorage.getFriends(id);
    }

    @Override
    public Collection<User> getCommonFriends(Long id, Long otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }

    @Override
    public User get(Long id) {
        return userStorage.get(id);
    }

    @Override
    public User create(User user) {
        return userStorage.add(user);
    }

    @Override
    public User update(User user) {
        return userStorage.update(user);
    }

    @Override
    public Collection<User> getAll() {
        return userStorage.getAll();
    }
}
