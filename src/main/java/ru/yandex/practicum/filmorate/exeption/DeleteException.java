package ru.yandex.practicum.filmorate.exeption;

public class DeleteException extends RuntimeException {
    public DeleteException(String message){
        super(message);
    }
}
