package com.collaberadigital.librarysystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an operation involving a book is invalid.
 */
@Getter
@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidBookException extends RuntimeException {
    public InvalidBookException(final String message) {
        super(message);
    }
}
