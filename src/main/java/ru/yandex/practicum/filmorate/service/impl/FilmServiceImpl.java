package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
public class FilmServiceImpl implements FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage, UserStorage userStorage) {

        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Film likeFilm(Long id, Long userId) {
        if (userStorage.contains(userId)) {
            return filmStorage.likeFilm(id, userId);
        } else {
            throw new NotExistException(String.format("Пользователя с id %s не существует. ", userId));
        }
    }

    @Override
    public Film deleteLike(Long id, Long userId) {
        if (userStorage.contains(userId)) {
            return filmStorage.deleteLike(id, userId);
        } else {
            throw new NotExistException(String.format("Пользователя с id %s не существует. ", userId));
        }
    }

    @Override
    public Collection<Film> getPopular(Long count) {
        return filmStorage.getPopular(count);
    }

    @Override
    public Film get(Long id) {
        return filmStorage.get(id);
    }

    @Override
    public Film create(Film film) {
        return filmStorage.add(film);
    }

    @Override
    public Film update(Film film) {
        return filmStorage.update(film);
    }

    @Override
    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }
}
