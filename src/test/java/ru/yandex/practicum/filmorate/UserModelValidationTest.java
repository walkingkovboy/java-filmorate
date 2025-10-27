package ru.yandex.practicum.filmorate;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserModelValidationTest {
    private Validator validator;

    @BeforeEach
    void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @DisplayName("Проверка валидного пользователя")
    @Test
    void validUser_shouldHaveNoViolations() {
        User user = new User();
        user.setEmail("alex@yandex.ru");
        user.setLogin("alex");
        user.setName("Алекс");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        var violations = validator.validate(user);
        assertEquals(0, violations.size(), "Ожидали отсутствие ошибок валидации");
    }

    @DisplayName("Проверка пустогй почты")
    @Test
    void email_blank_shouldFail() {
        User user = new User();
        user.setEmail("   ");
        user.setLogin("alex");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        var violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")),
                "Должна быть ошибка по полю email");
    }

    @DisplayName("Проверка неправильной почты")
    @Test
    void email_wrong_format_shouldFail() {
        User user = new User();
        user.setEmail("это-неправильный?эмейл@.");
        user.setLogin("alex");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        var violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")),
                "Должна быть ошибка по полю email (неверный формат)");
    }

    @DisplayName("Проверка пустого логина")
    @Test
    void login_blank_shouldFail() {
        User user = new User();
        user.setEmail("alex@yandex.ru");
        user.setLogin("   ");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        var violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("login")),
                "Должна быть ошибка по полю login (пустой)");
    }

    @DisplayName("Проверка пробела в логине")
    @Test
    void login_with_space_shouldFail() {
        User user = new User();
        user.setEmail("alex@yandex.ru");
        user.setLogin("alex good");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        var violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("login")),
                "Должна быть ошибка по полю login (содержит пробел)");
    }

    @DisplayName("День рождение в будушем")
    @Test
    void birthday_in_future_shouldFail() {
        User user = new User();
        user.setEmail("alex@yandex.ru");
        user.setLogin("alex");
        user.setBirthday(LocalDate.now().plusDays(1));

        var violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("birthday")),
                "Должна быть ошибка по полю birthday (в будущем)");
    }

    @DisplayName("Проверка пустого имени")
    @Test
    void name_canBeBlank_okBecauseControllerWillFillLogin() {
        User user = new User();
        user.setEmail("alex@yandex.ru");
        user.setLogin("alex");
        user.setName("");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        var violations = validator.validate(user);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("name")),
                "Поле name разрешено оставлять пустым на уровне модели");
    }
}
