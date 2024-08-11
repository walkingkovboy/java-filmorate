import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

public class FiimControllerTest {
    private static final String CORRECT_MAIL = "mail@mail.ru";
    private static final String CORRECT_LOGIN = "login";
    private Validator validator;
    Film film;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        factory.getConstraintValidatorFactory();
        validator = factory.getValidator();
        film = new Film();
    }

    @Test
    public void blankNameShouldFailValidation() {
        film.setName("");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void correctNameShouldPassValidation() {
        film.setName("Гарри Поттер");
        film.setDuration(130);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void descriptionMore200SymboldShouldFailValidation() {
        film.setName("Гарри Поттер");
        film.setDescription("1".repeat(201));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void dateBefor28December1895ShouldFailValidation() {
        film.setName("Гарри Поттер");
        film.setReleaseDate(LocalDate.of(1800, 1, 1));
        film.setDuration(130);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void correctDateShouldPassValidation() {
        film.setName("Гарри Поттер");
        film.setReleaseDate(LocalDate.of(1988, 12, 25));
        film.setDuration(130);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void negativeDurationShouldFailValidation() {
        film.setName("Гарри Поттер");
        film.setDuration(-10);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void zeroDurationShouldFailValidation() {
        film.setName("Гарри Поттер");
        film.setDuration(0);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void positiveDurationShouldPassValidation() {
        film.setName("Гарри Поттер");
        film.setDuration(100);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertTrue(violations.isEmpty());
    }
}