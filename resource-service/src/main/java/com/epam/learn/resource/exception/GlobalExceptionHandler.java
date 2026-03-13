package com.epam.learn.resource.exception;

import com.epam.learn.resource.model.ApiError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiError apiError = new ApiError(String.valueOf(HttpStatus.NOT_FOUND.value()), ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ParsingIdException.class)
    public ResponseEntity<ApiError> handleParsingIdException(ParsingIdException ex) {
        ApiError apiError = new ApiError(String.valueOf(HttpStatus.BAD_REQUEST.value()), ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        ConstraintViolation<?> constraintViolation = ex.getConstraintViolations().iterator().next();
        if (constraintViolation == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        ApiError error = new ApiError(
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                "Invalid value '%s' for ID. %s".formatted(
                        constraintViolation.getInvalidValue(),
                        constraintViolation.getMessage()));

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ApiError error = new ApiError(
                String.valueOf(status.value()),
                "Invalid value '%s' for ID. Must be a positive integer".formatted(ex.getValue()));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiError error = new ApiError(
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                "Invalid file format: application/json. Only MP3 files are allowed");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
