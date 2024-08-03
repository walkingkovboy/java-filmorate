package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> usersMap = new HashMap<>();
    private Long userIdCounter = 1L;

    @GetMapping
    public Collection<User> findAll() {
        return usersMap.values();
    }

    private Long getNextId() {
        return userIdCounter++;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Электронная почта не может быть пустой");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        Long userId = getNextId();
        user.setId(userId);
        usersMap.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User updatedUser) {
        Long userId = updatedUser.getId();
        if (userId == null) {
            throw new ValidationException("Айди не может быть пустым");
        }
        User storedUser = usersMap.get(updatedUser.getId());
        if (updatedUser.getEmail() != null) {
            if (!updatedUser.getEmail().contains("@")) {
                throw new ValidationException("Электронная почта должна содержать символ @");
            }
            storedUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getLogin() != null) {
            if (updatedUser.getLogin().contains(" ")) {
                throw new ValidationException("Логин не может содержать пробелы");
            }
            storedUser.setLogin(updatedUser.getLogin());
        }
        if (updatedUser.getName() != null) {
            storedUser.setName(updatedUser.getName());
        }
        if (updatedUser.getBirthday() != null) {
            if (updatedUser.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("Дата рождения не может быть в будущем");
            }
            storedUser.setBirthday(updatedUser.getBirthday());
        }
        return storedUser;
    }
}
