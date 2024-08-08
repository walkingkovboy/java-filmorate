package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserStorage userStorage;

    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User addFriend(Long id, Long friendId) {
        return userStorage.addFriend(id, friendId);
    }

    @Override
    public User deleteFriend(Long id, Long friendId) {
        return userStorage.deleteFriend(id, friendId);
    }

    @Override
    public Collection<User> getFriends(Long id) {
        return userStorage.getFriends(id);
    }

    @Override
    public Collection<User> getCommonFriends(Long id, Long otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }

    @Override
    public Boolean contains(Long id) {
        return userStorage.contains(id);
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
