package ru.yandex.practicum.filmorate;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserModelValidationTest {
    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @DisplayName("Проверка валидного пользователя")
    @Test
    void validUser_shouldHaveNoViolations() {
        User u = new User();
        u.setEmail("alex@yandex.ru");
        u.setLogin("alex");
        u.setName("Алекс");
        u.setBirthday(LocalDate.of(2000, 1, 1));

        var violations = validator.validate(u);
        assertEquals(0, violations.size(), "Ожидали отсутствие ошибок валидации");
    }

    @DisplayName("Проверка пустогй почты")
    @Test
    void email_blank_shouldFail() {
        User u = new User();
        u.setEmail("   ");
        u.setLogin("alex");
        u.setBirthday(LocalDate.of(2000, 1, 1));

        var violations = validator.validate(u);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")),
                "Должна быть ошибка по полю email");
    }

    @DisplayName("Проверка неправильной почты")
    @Test
    void email_wrong_format_shouldFail() {
        User u = new User();
        u.setEmail("это-неправильный?эмейл@.");
        u.setLogin("alex");
        u.setBirthday(LocalDate.of(2000, 1, 1));

        var violations = validator.validate(u);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")),
                "Должна быть ошибка по полю email (неверный формат)");
    }

    @DisplayName("Проверка пустого логина")
    @Test
    void login_blank_shouldFail() {
        User u = new User();
        u.setEmail("alex@yandex.ru");
        u.setLogin("   ");
        u.setBirthday(LocalDate.of(2000, 1, 1));

        var violations = validator.validate(u);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("login")),
                "Должна быть ошибка по полю login (пустой)");
    }

    @DisplayName("Проверка пробела в логине")
    @Test
    void login_with_space_shouldFail() {
        User u = new User();
        u.setEmail("alex@yandex.ru");
        u.setLogin("alex good");
        u.setBirthday(LocalDate.of(2000, 1, 1));

        var violations = validator.validate(u);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("login")),
                "Должна быть ошибка по полю login (содержит пробел)");
    }

    @DisplayName("День рождение в будушем")
    @Test
    void birthday_in_future_shouldFail() {
        User u = new User();
        u.setEmail("alex@yandex.ru");
        u.setLogin("alex");
        u.setBirthday(LocalDate.now().plusDays(1));

        var violations = validator.validate(u);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("birthday")),
                "Должна быть ошибка по полю birthday (в будущем)");
    }

    @DisplayName("Проверка пустого имени")
    @Test
    void name_canBeBlank_okBecauseControllerWillFillLogin() {
        User u = new User();
        u.setEmail("alex@yandex.ru");
        u.setLogin("alex");
        u.setName("");
        u.setBirthday(LocalDate.of(2000, 1, 1));

        var violations = validator.validate(u);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("name")),
                "Поле name разрешено оставлять пустым на уровне модели");
    }
}
