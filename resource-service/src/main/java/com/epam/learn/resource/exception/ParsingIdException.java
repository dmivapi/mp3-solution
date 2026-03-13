package com.epam.learn.resource.exception;

public class ParsingIdException extends RuntimeException {

    private static final String ERROR_MESSAGE_TEMPLATE = "Provided ID=%s is not a positive number";

    public ParsingIdException(String id) {
        super(ERROR_MESSAGE_TEMPLATE.formatted(id));
    }
}
