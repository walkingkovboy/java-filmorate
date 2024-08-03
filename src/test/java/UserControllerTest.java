import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserControllerTest {
    private UserController userController = new UserController();

    @Test
    void findAllUsersInitiallyEmpty() {
        Collection<User> allUsers = userController.findAll();
        assertTrue(allUsers.isEmpty());
    }

    @Test
    void createWithValidDataReturnsUser() {
        User user = new User();
        user.setLogin("user123");
        user.setEmail("user123@example.com");
        user.setBirthday(LocalDate.of(1990, 5, 15));
        User createdUser = userController.create(user);
        assertEquals(user.getLogin(), createdUser.getLogin());
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(user.getBirthday(), createdUser.getBirthday());
    }

    @Test
    void createWithInvalidEmailThrowsValidationException() {
        User user = new User();
        user.setLogin("user123");
        user.setEmail("invalid_email_example.com");
        user.setBirthday(LocalDate.of(1990, 5, 15));
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertTrue(exception.getMessage().contains("Электронная почта должна содержать символ @"));
    }
    @Test
    void createWithInvalidEmailThrowsValidationExceptionisBlank() {
        User user = new User();
        user.setLogin("user123");
        user.setEmail("         ");
        user.setBirthday(LocalDate.of(1990, 5, 15));
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertTrue(exception.getMessage().contains("Электронная почта не может быть пустой"));
    }
    @Test
    void createWithInvalidLoginThrowsValidationException() {
        User user = new User();
        user.setLogin("  ");
        user.setEmail("user123@example.com");
        user.setBirthday(LocalDate.of(1990, 5, 15));
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertTrue(exception.getMessage().contains("Логин не может быть пустым и содержать пробелы"));
    }
    @Test
    void createWithInvalidLoginisBlankThrowsValidationException() {
        User user = new User();
        user.setLogin("user1 23");
        user.setEmail("user123@example.com");
        user.setBirthday(LocalDate.of(1990, 5, 15));
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertTrue(exception.getMessage().contains("Логин не может быть пустым и содержать пробелы"));
    }
    @Test
    void createWithFutureBirthdayThrowsValidationException() {
        User user = new User();
        user.setLogin("user123");
        user.setEmail("user123@example.com");
        user.setBirthday(LocalDate.of(2990, 5, 15));
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertTrue(exception.getMessage().contains("Дата рождения не может быть в будущем"));
    }
    @Test
    void updateUserWithValidDataReturnsUpdatedUser() {
        User user = new User();
        user.setLogin("user123");
        user.setEmail("user123@example.com");
        user.setBirthday(LocalDate.of(1990, 5, 15));
        userController.create(user);
        User updateUser = new User();
        updateUser.setId(1L);
        updateUser.setLogin("user1234");
        updateUser.setEmail("user1234@example.com");
        updateUser.setBirthday(LocalDate.of(1995, 5, 15));
        User result = userController.update(updateUser);
        assertEquals("user1234", result.getLogin());
        assertEquals("user1234@example.com", result.getEmail());
        assertEquals(LocalDate.of(1995, 5, 15), result.getBirthday());
    }
}
