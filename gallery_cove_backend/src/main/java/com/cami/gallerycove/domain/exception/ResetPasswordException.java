package com.cami.gallerycove.domain.exception;

public class ResetPasswordException extends RuntimeException {

    public ResetPasswordException() {
        super();
    }

    public ResetPasswordException(String message) {
        super(message);
    }

    public ResetPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
