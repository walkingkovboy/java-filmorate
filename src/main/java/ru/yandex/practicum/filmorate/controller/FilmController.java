package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> filmsMap = new HashMap<>();
    private Long filmIdCounter = 1L;

    private Long getNextId() {
        return filmIdCounter++;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmsMap.values();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
        Long filmId = getNextId();
        film.setId(filmId);
        filmsMap.put(filmId, film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film updateFilm) {
        Long filmId = updateFilm.getId();
        if (filmId == null) {
            throw new ValidationException("Айди не может быть пустым");
        }
        Film storedFilm = filmsMap.get(filmId);
        if (updateFilm.getName() != null) {
            storedFilm.setName(updateFilm.getName());
        }
        if (updateFilm.getDescription() != null) {
            if (updateFilm.getDescription().length() > 200) {
                throw new ValidationException("Максимальная длина описания — 200 символов");
            }
            storedFilm.setDescription(updateFilm.getDescription());
        }
        if (updateFilm.getReleaseDate() != null) {
            if (updateFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
            }
            storedFilm.setReleaseDate(updateFilm.getReleaseDate());
        }
        if (updateFilm.getDuration() != 0) {
            if (updateFilm.getDuration() < 0) {
                throw new ValidationException("Продолжительность фильма должна быть положительным числом");
            }
            storedFilm.setDuration(updateFilm.getDuration());
        }
        return storedFilm;
    }

}
