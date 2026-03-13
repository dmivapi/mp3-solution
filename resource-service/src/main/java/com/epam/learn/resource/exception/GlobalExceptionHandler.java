package com.epam.learn.resource.exception;

import com.epam.learn.resource.model.ApiError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Size;
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

import java.util.Map;

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
}
