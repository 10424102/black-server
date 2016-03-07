package org.projw.blackserver.controllers.exceptions;

public class RequestDataFormatException extends BusinessException {
    public RequestDataFormatException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return 0;
    }

    @Override
    public String getErrorMessage() {
        return null;
    }
}
