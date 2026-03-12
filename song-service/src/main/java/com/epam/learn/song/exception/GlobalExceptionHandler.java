package com.epam.learn.song.exception;

import com.epam.learn.song.model.ApiError;
import com.epam.learn.song.model.ValidationApiError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Map<String, String> FIELD_NAMES = Map.ofEntries(
            Map.entry("name",     "Song name"),
            Map.entry("artist",   "Artist name"),
            Map.entry("album",    "Album name"),
            Map.entry("duration", "Duration"),
            Map.entry("year",     "Year")
    );

    @ExceptionHandler(SongNotFoundException.class)
    public ResponseEntity<ApiError> handleSongNotFoundException(SongNotFoundException ex) {
        ApiError apiError = new ApiError(String.valueOf(HttpStatus.NOT_FOUND.value()),ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SongAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleSongAlreadyExistsException(SongAlreadyExistsException ex) {
        ApiError apiError = new ApiError(String.valueOf(HttpStatus.CONFLICT.value()),ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(ParsingIdException.class)
    public ResponseEntity<ApiError> handleParsingIdException(ParsingIdException ex) {
        ApiError apiError = new ApiError(String.valueOf(HttpStatus.BAD_REQUEST.value()),ex.getMessage());

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
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders
                    headers, HttpStatusCode status, WebRequest request) {

        Map<String, String > details = ex.getBindingResult().getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            this::getReadableMessage
                    ));

        ValidationApiError validationApiError = new ValidationApiError(
                String.valueOf(status.value()),
                "Validation error",
                details
        );

        return new ResponseEntity<>(validationApiError, status);
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleTypeMismatch(
        TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ApiError error = new ApiError(
                String.valueOf(status.value()),
                "Invalid value '%s' for ID. Must be a positive integer".formatted(ex.getValue()));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    String getReadableMessage(FieldError fieldError) {
        if (fieldError.getRejectedValue() != null) {
            return Objects.requireNonNullElse(fieldError.getDefaultMessage(), "invalid value");
        }

        return FIELD_NAMES.getOrDefault(fieldError.getField(), fieldError.getField()) + " is required";
    }
}
