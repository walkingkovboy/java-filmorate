package ru.yandex.practicum.filmorate;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmModelValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @DisplayName("Валидный фильм проходит без ошибок")
    @Test
    void validFilm_shouldHaveNoViolations() {
        Film f = new Film();
        f.setName("Властелин колец");
        f.setDescription("Эпическая сага о кольце всевластия");
        f.setReleaseDate(LocalDate.of(2001, 12, 19));
        f.setDuration(178);

        var violations = validator.validate(f);
        assertEquals(0, violations.size(), "Ожидали отсутствие ошибок валидации");
    }

    @DisplayName("Название не может быть пустым")
    @Test
    void name_blank_shouldFail() {
        Film f = new Film();
        f.setName("   ");
        f.setDescription("Фэнтези");
        f.setReleaseDate(LocalDate.of(2001, 12, 19));
        f.setDuration(100);

        var violations = validator.validate(f);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")),
                "Должна быть ошибка по полю name");
    }

    @DisplayName("Описание больше 200 символов")
    @Test
    void description_over200_shouldFail() {
        Film f = new Film();
        f.setName("Властелин колец");
        f.setDescription("a".repeat(201));
        f.setReleaseDate(LocalDate.of(2001, 12, 19));
        f.setDuration(100);

        var violations = validator.validate(f);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")),
                "Должна быть ошибка по полю description");
    }

    @DisplayName("Продолжительность должна быть положительной")
    @Test
    void duration_nonPositive_shouldFail() {
        Film f = new Film();
        f.setName("Властелин колец");
        f.setDescription("Фэнтези");
        f.setReleaseDate(LocalDate.of(2001, 12, 19));
        f.setDuration(0);

        var violations = validator.validate(f);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("duration")),
                "Должна быть ошибка по полю duration");
    }

    @DisplayName("Дата релиза обязательна")
    @Test
    void releaseDate_null_shouldFail() {
        Film f = new Film();
        f.setName("Властелин колец");
        f.setDescription("Фэнтези");
        f.setReleaseDate(null);
        f.setDuration(100);

        var violations = validator.validate(f);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("releaseDate")),
                "Должна быть ошибка по полю releaseDate");
    }
}
