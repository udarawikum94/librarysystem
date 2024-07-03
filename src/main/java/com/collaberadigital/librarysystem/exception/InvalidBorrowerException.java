package com.collaberadigital.librarysystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an operation involving a borrower is invalid.
 */
@Getter
@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidBorrowerException extends RuntimeException {
    public InvalidBorrowerException(final String message) {
        super(message);
    }
}
