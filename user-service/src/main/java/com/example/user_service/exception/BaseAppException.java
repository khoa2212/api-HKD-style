package com.example.user_service.exception;

import lombok.Getter;

@Getter
public class BaseAppException extends  Exception {
    protected final ErrorMessage errorMessage;
    public BaseAppException(String message, String statusCode) {
        super(message);
        this.errorMessage = new ErrorMessage(message, statusCode);
    }
}
