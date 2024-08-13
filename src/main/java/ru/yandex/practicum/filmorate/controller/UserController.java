package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.FriendRequest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;
    private final FriendService friendService;

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAll();
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        return userService.create(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        return userService.update(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) throws NotExistException {
        return userService.get(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public FriendRequest addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return friendService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public FriendRequest deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return friendService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable Long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
