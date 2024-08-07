import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class FiimControllerTest {
    private FilmController filmController = new FilmController();

    @Test
    void findAllFilmsInitiallyEmpty() {
        Collection<Film> allFilms = filmController.findAll();
        assertTrue(allFilms.isEmpty());
    }

    @Test
    void addFilmWithInvalidNameThrowsValidationException() {
        Film film = new Film();
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertTrue(exception.getMessage().contains("Название фильма не может быть пустым"));
    }

    @Test
    void addFilmWithLongDescriptionThrowsValidationException() {
        Film film = new Film();
        film.setName("Гарри Поттер");
        film.setDescription("1".repeat(201));
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertTrue(exception.getMessage().contains("Максимальная длина описания — 200 символов"));
    }

    @Test
    void addFilmWithNegativeDurationThrowsValidationException() {
        Film film = new Film();
        film.setName("Гарри Поттер");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(-148);
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertTrue(exception.getMessage().contains("Продолжительность фильма должна быть положительным числом"));
    }

    @Test
    void updateFilmWithoutIdThrowsValidationException() {
        Film film = new Film();
        film.setName("Гарри Поттер");
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.updateFilm(film));
        assertTrue(exception.getMessage().contains("Айди не может быть пустым"));
    }

    @Test
    void updateFilmWithValidDataReturnsUpdatedFilm() {
        Film existingFilm = new Film();
        existingFilm.setName("Гарри Поттер");
        existingFilm.setDescription("Описание фильма");
        existingFilm.setReleaseDate(LocalDate.of(2010, 7, 16));
        existingFilm.setDuration(148);
        filmController.addFilm(existingFilm);
        Film updatedFilm = new Film();
        updatedFilm.setId(1L);
        updatedFilm.setName("Гарри Поттер 2");
        updatedFilm.setDescription("Новое описание фильма");
        updatedFilm.setReleaseDate(LocalDate.of(2010, 7, 16));
        updatedFilm.setDuration(160);
        Film result = filmController.updateFilm(updatedFilm);
        assertEquals("Гарри Поттер 2", result.getName());
        assertEquals("Новое описание фильма", result.getDescription());
        assertEquals(LocalDate.of(2010, 7, 16), result.getReleaseDate());
        assertEquals(160, result.getDuration());
    }
}
