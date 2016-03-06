package org.projw.blackserver.controllers.exceptions;

public abstract class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super("【业务逻辑异常】" + message);
    }

    public abstract int getErrorCode();

    public abstract String getErrorMessage();
}
