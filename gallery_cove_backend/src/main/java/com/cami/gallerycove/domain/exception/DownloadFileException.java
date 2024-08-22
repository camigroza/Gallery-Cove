package com.cami.gallerycove.domain.exception;

public class DownloadFileException extends RuntimeException {

    public DownloadFileException() {
        super();
    }

    public DownloadFileException(String message) {
        super(message);
    }

    public DownloadFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
