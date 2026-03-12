package com.epam.learn.song.exception;

import com.epam.learn.song.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SongNotFoundException.class)
    public ResponseEntity<ApiError> handleSongNotFoundException(SongNotFoundException ex) {
        ApiError apiError = new ApiError(String.valueOf(HttpStatus.NOT_FOUND.value()),ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
}
