package com.epam.learn.song.exception;

import com.epam.learn.song.model.ApiError;
import com.epam.learn.song.model.ValidationApiError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Size;
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception ex) {
        ApiError apiError = new ApiError(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), "Internal server error");

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

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

        if (ex.getConstraintViolations().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ConstraintViolation<?> constraintViolation = ex.getConstraintViolations().iterator().next();

        if (isSizeConstraint(constraintViolation)) {
            return handleSizeConstraintViolation(constraintViolation);
        }

        ApiError error = new ApiError(
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                "Invalid value '%s' for ID. %s".formatted(
                        constraintViolation.getInvalidValue(),
                        constraintViolation.getMessage()));

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    private boolean isSizeConstraint(ConstraintViolation<?> violation) {
        return violation.getConstraintDescriptor().getAnnotation() instanceof Size;
    }

    private ResponseEntity<Object> handleSizeConstraintViolation(ConstraintViolation<?> constraintViolation) {
        Map<String, Object> attributes = constraintViolation.getConstraintDescriptor().getAttributes();
        String invalidValue = String.valueOf(constraintViolation.getInvalidValue());
        int minSize = (int) attributes.get("min");
        int maxSize = (int) attributes.get("max");

        String message;
        if (invalidValue.length() > maxSize) {
            message = "CSV string is too long: received %d characters, maximum allowed is %d"
                    .formatted(invalidValue.length(), maxSize);
        } else if (invalidValue.length() < minSize) {
            message = "CSV string is too short: received %d characters, minimum required is %d"
                    .formatted(invalidValue.length(), minSize);
        } else {
            message = constraintViolation.getMessage();
        }

        ApiError error = new ApiError(String.valueOf(HttpStatus.BAD_REQUEST.value()), message);
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
