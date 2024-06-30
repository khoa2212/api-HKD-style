package com.example.user_service.exception;

public class InvalidURLTokenException extends BaseAppException {
    public InvalidURLTokenException(String message) {
        super(message, "InvalidURLToken");
    }
}