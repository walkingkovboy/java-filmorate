package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotValidRequest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    @Autowired
    @Qualifier("filmDbStorage")
    private FilmStorage filmStorage;
    @Autowired
    @Qualifier("likeDbStorage")
    private LikeStorage likeStorage;
    @Autowired
    @Qualifier("ratingDbStorage")
    private RatingStorage ratingStorage;
    @Autowired
    @Qualifier("genreDbStorage")
    private GenreStorage genreStorage;

    public FilmServiceImpl(FilmStorage filmStorage) {

        this.filmStorage = filmStorage;
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
        if (film.getMpa() != null) {
            try {
                ratingStorage.get(film.getMpa().getId());
            } catch (Exception e) {
                throw new NotValidRequest("Rating Id not exist");
            }
        }
        if (film.getGenres() != null) {
            try {
                film.getGenres().stream()
                        .forEach(genre -> genreStorage.get(genre.getId()));
            } catch (Exception e) {
                throw new NotValidRequest("Genre Id not exist");
            }
        }
        film.setGenres(film.getGenres().stream().distinct().collect(Collectors.toList()));
        return filmStorage.add(film);
    }

    @Override
    public Film update(Film film) {
        film.setGenres(film.getGenres().stream().distinct().collect(Collectors.toList()));
        return filmStorage.update(film);
    }

    @Override
    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }
}
