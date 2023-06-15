package io.github.thewebcode.yplugin.exceptions;

public class InvalidChunkStateException extends Exception {
    public InvalidChunkStateException() {
    }

    public InvalidChunkStateException(String message) {
        super(message);
    }

    public InvalidChunkStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidChunkStateException(Throwable cause) {
        super(cause);
    }

    public InvalidChunkStateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
