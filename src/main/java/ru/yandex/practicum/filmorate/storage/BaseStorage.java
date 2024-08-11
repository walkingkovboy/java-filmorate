package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;

public interface BaseStorage<T> {
    T get(Long id);

    T add(T t);

    T remove(T t);

    T update(T t);

    Boolean contains(Long id);

    Collection<T> getAll();
}
