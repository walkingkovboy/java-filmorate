package ru.yandex.practicum.filmorate.exception;

public class NotValidRequest extends RuntimeException{
    public NotValidRequest(String message) {
        super(message);
    }
}
