package com.collaberadigital.librarysystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Represents a generic response handler for common exceptions.
 * This class encapsulates the error message to be returned in the response body.
 */
@Getter
@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class CommonSystemException extends RuntimeException {
    public CommonSystemException(final String message) {
        super(message);
    }
}
