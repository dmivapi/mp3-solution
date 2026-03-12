package com.epam.learn.resource.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE_TEMPLATE = "Resource with ID=%d not found";

    public ResourceNotFoundException(Long id) {
        super(ERROR_MESSAGE_TEMPLATE.formatted(id));
    }
}
