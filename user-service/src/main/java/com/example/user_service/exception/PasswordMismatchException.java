package com.example.user_service.exception;

public class PasswordMismatchException extends BaseAppException {
    public PasswordMismatchException(String message) {
        super(message, "PasswordMismatch");
    }
}
