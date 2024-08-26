package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikeService;

import java.util.Collection;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/films")
public class FilmController {
    private final FilmService filmService;
    private final LikeService likeService;

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmService.getAll();
    }

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        return filmService.update(film);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Long id) {
        return filmService.get(id);
    }

    @PutMapping("{id}/like/{userId}")
    public Like likeFilm(@PathVariable Long id, @PathVariable Long userId) {
        return likeService.likeFilm(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Like deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        return likeService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(@RequestParam(defaultValue = "10") Long count) {
        return filmService.getPopular(count);
    }
}
