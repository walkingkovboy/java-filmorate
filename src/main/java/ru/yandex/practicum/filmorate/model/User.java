package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class User {
    private Integer id;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;

    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "[^\\s]+", message = "Без пробелов")
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    public User() {
    }

    @JsonCreator
    public User(@JsonProperty("email") String email,
                @JsonProperty("login") String login,
                @JsonProperty("name") String name,
                @JsonProperty("birthday") LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = (name == null || name.isBlank()) ? login : name;
        this.birthday = birthday;
    }
}
