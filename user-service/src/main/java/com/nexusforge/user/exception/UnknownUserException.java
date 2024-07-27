package com.nexusforge.user.exception;

public class UnknownUserException extends RuntimeException{
    public static final String MESSAGE = "User [id=%d] is not found";

    public UnknownUserException(Integer userId) {
        super(MESSAGE.formatted(userId));
    }
}
