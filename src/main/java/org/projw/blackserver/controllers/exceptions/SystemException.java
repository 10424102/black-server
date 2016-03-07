package org.projw.blackserver.controllers.exceptions;

public class SystemException extends RuntimeException {
    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemException(String message) {
        super(message, null);
    }
}
