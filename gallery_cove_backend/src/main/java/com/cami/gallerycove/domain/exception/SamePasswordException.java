package com.cami.gallerycove.domain.exception;

public class SamePasswordException extends RuntimeException {

    public SamePasswordException() {
        super();
    }

    public SamePasswordException(String message) {
        super(message);
    }

    public SamePasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
