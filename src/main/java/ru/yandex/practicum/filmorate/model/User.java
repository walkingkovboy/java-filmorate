package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;
import ru.yandex.practicum.filmorate.constraint.WithoutSpaces;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id;
    @Email(message = "Адрес электронной почты указан неверно")
    @NotBlank(message = "Электронная почта должна содержать символ")
    private String email;
    @NotBlank(message = "Логин не может быть пустым и содержать пробелы")
    @WithoutSpaces
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();
}
