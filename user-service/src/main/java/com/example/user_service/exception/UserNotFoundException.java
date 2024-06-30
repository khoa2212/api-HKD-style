package com.example.user_service.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends BaseAppException {
    public UserNotFoundException(String message) {
        super(message, "UserNotFound");
    }

}
