package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmService extends BaseService<Film> {
    Film likeFilm(Long id, Long userId);

    Film deleteLike(Long id, Long userId);

    Collection<Film> getPopular(Long count);
}
