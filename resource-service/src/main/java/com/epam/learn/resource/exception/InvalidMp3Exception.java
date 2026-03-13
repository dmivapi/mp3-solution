package com.epam.learn.resource.exception;

public class InvalidMp3Exception extends RuntimeException {

    public InvalidMp3Exception() {
        super("Invalid MP3 file");
    }

    public InvalidMp3Exception(Throwable cause) {
        super("Invalid MP3 file", cause);
    }
}
