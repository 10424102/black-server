package org.projw.blackserver.controllers.exceptions;

public class UserNotFoundException extends SystemException {
    public UserNotFoundException(long userId) {
        super(String.format("找不到用户 (id = %d)", userId));
    }
}
