package com.epam.learn.song.exception;

public class SongAlreadyExistsException extends RuntimeException {

    private static final String ERROR_MESSAGE_TEMPLATE = "Metadata for resource ID=%d already exists";

    public SongAlreadyExistsException(Long id) {
        super(ERROR_MESSAGE_TEMPLATE.formatted(id));
    }
}
