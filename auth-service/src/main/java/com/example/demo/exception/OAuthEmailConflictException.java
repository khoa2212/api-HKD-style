package com.example.demo.exception;

import lombok.Getter;

@Getter
public class OAuthEmailConflictException extends RuntimeException {
    private final ErrorBody errorBody;
    public OAuthEmailConflictException(String message) {
        super(message);
        this.errorBody = new ErrorBody(message, "EmailAlreadyUsed");
    }
}
