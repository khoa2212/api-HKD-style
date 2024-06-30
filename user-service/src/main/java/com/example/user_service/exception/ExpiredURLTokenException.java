package com.example.user_service.exception;

public class ExpiredURLTokenException extends BaseAppException {
    public ExpiredURLTokenException(String message) {
        super(message, "ExpiredURLToken");
    }
}
