package com.example.apidemo.exception;

import lombok.Getter;

@Getter
public class ReviewNotFoundException extends Exception {
    private String message;
    private String code;

    public ReviewNotFoundException(String message, String code) {
        this.message = message;
        this.code = code;
    }
}
