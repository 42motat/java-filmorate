package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.dto.ErrorResponse;

public abstract class AbstractDtoException extends RuntimeException {
    private final HttpStatusCode httpStatusCode;

    public AbstractDtoException(String message, HttpStatusCode httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public ResponseEntity<ErrorResponse> toResponse() {
        ErrorResponse errorResponse = new ErrorResponse(getMessage());
        return new ResponseEntity<>(errorResponse, httpStatusCode);
    }

}
