package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;

public class ServerErrorException extends AbstractDtoException {
    public ServerErrorException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
