package org.team10424102.blackserver.exceptions;

public abstract class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super("【业务逻辑异常】" + message);
    }

    public abstract int getErrorCode();

    public abstract String getErrorMessage();
}
