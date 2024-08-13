package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> filmsMap = new HashMap<>();
    private Long filmIdCounter = 1L;
    @Autowired
    @Qualifier("userDbStorage")
    private UserStorage userStorage;
    private Long getNextId() {
        return filmIdCounter++;
    }

    public Film likeFilm(Long id, Long userId) {
        if (filmsMap.containsKey(id)) {
            Set<Long> likes = filmsMap.get(id).getLikesFromUsers();
            likes.add(userId);
            filmsMap.get(id).setLikesFromUsers(likes);
            return filmsMap.get(id);
        }
        throw new NotExistException(String.format("Фильма с id %s не существует", id));
    }

    public Film deleteLike(Long id, Long userId) {
        if (filmsMap.containsKey(id)) {
            Set<Long> likes = filmsMap.get(id).getLikesFromUsers();
            likes.remove(userId);
            filmsMap.get(id).setLikesFromUsers(likes);
            return filmsMap.get(id);
        }
        throw new NotExistException(String.format("Фильма с id %s не существует", id));
    }

    @Override
    public Collection<Film> getPopular(Long count) {
        return filmsMap.values().stream()
                .sorted(Comparator.comparing(Film::getPopularity).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film get(Long id) {
        if (filmsMap.containsKey(id)) {
            return filmsMap.get(id);
        }
        throw new NotExistException(String.format("Фильма с id %s не существует", id));
    }

    @Override
    public Film add(Film film) {
        Long filmId = getNextId();
        film.setId(filmId);
        filmsMap.put(filmId, film);
        return film;
    }

    @Override
    public Film remove(Film film) {
        return null;
    }

    @Override
    public Film update(Film film) {
        if (filmsMap.containsKey(film.getId())) {
            filmsMap.put(film.getId(), film);
            return film;
        } else {
            throw new NotExistException(String.format("Фильма с id %s не существует. ", film.getId()));
        }
    }

    @Override
    public Boolean contains(Long id) {
        return filmsMap.containsKey(id);
    }

    @Override
    public Collection<Film> getAll() {
        return filmsMap.values();
    }
}
