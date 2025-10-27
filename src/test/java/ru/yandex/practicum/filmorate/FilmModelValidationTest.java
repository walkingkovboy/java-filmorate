package ru.yandex.practicum.filmorate;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmModelValidationTest {

    private Validator validator;


    @BeforeEach
    void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @DisplayName("Валидный фильм проходит без ошибок")
    @Test
    void validFilm_shouldHaveNoViolations() {
        Film film = new Film();
        film.setName("Властелин колец");
        film.setDescription("Эпическая сага о кольце всевластия");
        film.setReleaseDate(LocalDate.of(2001, 12, 19));
        film.setDuration(178);

        var violations = validator.validate(film);
        assertEquals(0, violations.size(), "Ожидали отсутствие ошибок валидации");
    }

    @DisplayName("Название не может быть пустым")
    @Test
    void name_blank_shouldFail() {
        Film film = new Film();
        film.setName("   ");
        film.setDescription("Фэнтези");
        film.setReleaseDate(LocalDate.of(2001, 12, 19));
        film.setDuration(100);

        var violations = validator.validate(film);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")),
                "Должна быть ошибка по полю name");
    }

    @DisplayName("Описание больше 200 символов")
    @Test
    void description_over200_shouldFail() {
        Film film = new Film();
        film.setName("Властелин колец");
        film.setDescription("a".repeat(201));
        film.setReleaseDate(LocalDate.of(2001, 12, 19));
        film.setDuration(100);

        var violations = validator.validate(film);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")),
                "Должна быть ошибка по полю description");
    }

    @DisplayName("Продолжительность должна быть положительной")
    @Test
    void duration_nonPositive_shouldFail() {
        Film film = new Film();
        film.setName("Властелин колец");
        film.setDescription("Фэнтези");
        film.setReleaseDate(LocalDate.of(2001, 12, 19));
        film.setDuration(0);

        var violations = validator.validate(film);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("duration")),
                "Должна быть ошибка по полю duration");
    }

    @DisplayName("Дата релиза обязательна")
    @Test
    void releaseDate_null_shouldFail() {
        Film film = new Film();
        film.setName("Властелин колец");
        film.setDescription("Фэнтези");
        film.setReleaseDate(null);
        film.setDuration(100);

        var violations = validator.validate(film);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("releaseDate")),
                "Должна быть ошибка по полю releaseDate");
    }
}
