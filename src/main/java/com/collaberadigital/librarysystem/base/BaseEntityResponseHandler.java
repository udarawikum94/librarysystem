package com.collaberadigital.librarysystem.base;

import com.collaberadigital.librarysystem.dto.CommonResponseHandler;
import com.collaberadigital.librarysystem.exception.CommonSystemException;
import com.collaberadigital.librarysystem.exception.InvalidBookException;
import com.collaberadigital.librarysystem.exception.InvalidBorrowerException;
import com.collaberadigital.librarysystem.exception.ResourceNotFoundException;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Global exception handler for handling various types of exceptions in the application.
 * This class provides centralized exception handling for common exceptions that may occur
 * during API requests, such as method argument type mismatch, resource not found, invalid
 * borrower or book, common system exceptions, and general exceptions.
 */
@RestControllerAdvice
@NoArgsConstructor
public class BaseEntityResponseHandler {

    /**
     * Logger instance for logging messages.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseEntityResponseHandler.class);

    /**
     * Handles MethodArgumentTypeMismatchException and returns a ResponseEntity with
     * an appropriate error message and HTTP status code.
     * @param exception The MethodArgumentTypeMismatchException object.
     * @param request The WebRequest object.
     * @return ResponseEntity containing an error message and HTTP status code.
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> methodArgumentTypeMismatchException(
            final MethodArgumentTypeMismatchException exception, WebRequest request) {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error("Method Argument Type Mismatch Exception: {}", exception.getMessage());
        }

        return new ResponseEntity<>(
                new CommonResponseHandler(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles ResourceNotFoundException and returns a ResponseEntity with
     * an appropriate error message and HTTP status code.
     * @param exception The ResourceNotFoundException object.
     * @return ResponseEntity containing an error message and HTTP status code.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CommonResponseHandler> handleResourceNotFoundException(
            final ResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new CommonResponseHandler(exception.getMessage()));
    }

    /**
     * Handles InvalidBorrowerException and returns a ResponseEntity with
     * an appropriate error message and HTTP status code.
     * @param exception The InvalidBorrowerException object.
     * @return ResponseEntity containing an error message and HTTP status code.
     */
    @ExceptionHandler(InvalidBorrowerException.class)
    public ResponseEntity<CommonResponseHandler> handleInvalidBorrowerException(
            final InvalidBorrowerException exception) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new CommonResponseHandler(exception.getMessage()));
    }

    /**
     * Handles InvalidBookException and returns a ResponseEntity with
     * an appropriate error message and HTTP status code.
     * @param exception The InvalidBookException object.
     * @return ResponseEntity containing an error message and HTTP status code.
     */
    @ExceptionHandler(InvalidBookException.class)
    public ResponseEntity<CommonResponseHandler> handleInvalidBookException(
            final InvalidBookException exception) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new CommonResponseHandler(exception.getMessage()));
    }

    /**
     * Handles CommonSystemException and returns a ResponseEntity with
     * an appropriate error message and HTTP status code.
     * @param exception The CommonSystemException object.
     * @return ResponseEntity containing an error message and HTTP status code.
     */
    @ExceptionHandler(CommonSystemException.class)
    public ResponseEntity<CommonResponseHandler> handleCommonSystemException(
            final CommonSystemException exception) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new CommonResponseHandler(exception.getMessage()));
    }

    /**
     * Handles general exceptions and returns a ResponseEntity with
     * an appropriate error message and HTTP status code.
     * @param exception The Exception object.
     * @return ResponseEntity containing an error message and HTTP status code.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponseHandler> handleGeneralException(
            final Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CommonResponseHandler(exception.getMessage()));
    }

    /**
     * Handles HttpRequestMethodNotSupportedException and returns a ResponseEntity with
     * an appropriate error message and HTTP status code.
     * @param exception The HttpRequestMethodNotSupportedException object.
     * @param request The WebRequest object.
     * @return ResponseEntity containing an error message and HTTP status code.
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<Object> httpRequestMethodNotSupportedException(
            final MethodArgumentTypeMismatchException exception, WebRequest request) {
        return new ResponseEntity<>(
                new CommonResponseHandler(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles MethodArgumentNotValidException and returns a ResponseEntity with
     * validation errors along with HTTP status code.
     * @param exception The MethodArgumentNotValidException object.
     * @return ResponseEntity containing validation errors and HTTP status code.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            final MethodArgumentNotValidException exception) {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error("Method Argument Not Valid Exception: {}", exception.getMessage());
        }

        Map<String, String> errors = new ConcurrentHashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            final String fieldName = ((FieldError) error).getField();
            final String errorMessage = error.getDefaultMessage();

            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
