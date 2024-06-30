package com.example.user_service.exception;

public class SendMailException extends RuntimeException {
    private final ErrorMessage errorMessage;
    public SendMailException(String message) {
        super(message);
        errorMessage = new ErrorMessage(message, "SendMailError");
    }
}
