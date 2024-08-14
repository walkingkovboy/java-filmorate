package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

public interface GenreStorage extends BaseStorage<Genre> {
    Boolean containsAll(Set<Long> ids);
}
