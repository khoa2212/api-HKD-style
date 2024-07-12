package com.example.demo.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends Exception {
    private final  ErrorBody errorBody;
    public UserNotFoundException(String message) {
        super(message);
        this.errorBody = new ErrorBody(message, "UserNotFound");
    }
}
