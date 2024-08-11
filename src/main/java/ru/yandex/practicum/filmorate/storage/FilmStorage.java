package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage extends BaseStorage<Film> {
    Film likeFilm(Long id, Long userId);

    Film deleteLike(Long id, Long userId);

    Collection<Film> getPopular(Long count);
}
