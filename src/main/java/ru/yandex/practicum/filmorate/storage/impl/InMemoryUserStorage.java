package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> usersMap = new HashMap<>();
    private Long userIdCounter = 1L;

    private Long getNextId() {
        return userIdCounter++;
    }


    public User addFriend(Long id, Long friendId) {
        if (usersMap.containsKey(id) && usersMap.containsKey(friendId)) {
            Set<Long> friends = usersMap.get(id).getFriends();
            friends.add(friendId);
            usersMap.get(id).setFriends(friends);
            friends = usersMap.get(friendId).getFriends();
            friends.add(id);
            usersMap.get(friendId).setFriends(friends);
            return usersMap.get(id);
        }
        throw new NotExistException(String.format("Пользователя с id {} или {} не существует", id, friendId));
    }

    public User deleteFriend(Long id, Long friendId) {
        if (usersMap.containsKey(id) && usersMap.containsKey(friendId)) {
            Set<Long> friends = usersMap.get(id).getFriends();
            friends.remove(friendId);
            usersMap.get(id).setFriends(friends);
            friends = usersMap.get(friendId).getFriends();
            friends.remove(id);
            usersMap.get(friendId).setFriends(friends);
            return usersMap.get(id);
        }
        throw new NotExistException(String.format("Пользователя с id {} или {} не существует", id, friendId));
    }

    @Override
    public Collection<User> getFriends(Long id) {
        if (usersMap.containsKey(id)) {
            Set<Long> friends = usersMap.get(id).getFriends();
            return friends.stream().map(i -> usersMap.get(i)).collect(Collectors.toList());
        }
        throw new NotExistException(String.format("Пользователя с id %s не существует", id));
    }

    @Override
    public Collection<User> getCommonFriends(Long id, Long otherId) {
        if (usersMap.containsKey(id) && usersMap.containsKey(otherId)) {
            Set<Long> friends = usersMap.get(id).getFriends();
            Set<Long> otherFriends = usersMap.get(otherId).getFriends();
            Set<Long> intersection = new HashSet<>(friends);
            intersection.retainAll(otherFriends);
            return intersection.stream().map(i -> usersMap.get(i)).collect(Collectors.toList());
        }
        throw new NotExistException(String.format("Пользователя с id {} или {} не существует", id, otherId));
    }

    @Override
    public User get(Long id) {
        if (usersMap.containsKey(id)) {
            return usersMap.get(id);
        }
        throw new NotExistException(String.format("Пользователя с id %s не существует", id));
    }

    @Override
    public User add(User user) {
        Long userId = getNextId();
        user.setId(userId);
        usersMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User remove(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        if (usersMap.containsKey(user.getId())) {
            usersMap.put(user.getId(), user);
            return user;
        } else {
            throw new NotExistException(String.format("Пользователя с id %s не существует. ", user.getId()));
        }
    }

    @Override
    public Boolean contains(Long id) {
        return usersMap.containsKey(id);
    }

    @Override
    public Collection<User> getAll() {
        return usersMap.values();
    }
}
