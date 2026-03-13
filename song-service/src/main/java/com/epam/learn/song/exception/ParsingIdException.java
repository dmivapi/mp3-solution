package com.epam.learn.song.exception;

public class ParsingIdException extends RuntimeException {

    private static final String ERROR_MESSAGE_TEMPLATE = "Invalid ID format: '%s'. Only positive integers are allowed";

    public ParsingIdException(String id) {
        super(ERROR_MESSAGE_TEMPLATE.formatted(id));
    }
}
