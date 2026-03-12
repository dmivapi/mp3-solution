package com.epam.learn.song.exception;

public class SongNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE_TEMPLATE = "Song metadata for ID=%d not found";

    public SongNotFoundException(Long id) {
        super(ERROR_MESSAGE_TEMPLATE.formatted(id));
    }
}
