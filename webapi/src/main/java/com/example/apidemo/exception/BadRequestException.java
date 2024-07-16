package com.example.apidemo.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends Exception {
    private String message;
    private String code;

    public BadRequestException(String message, String code) {
        this.message = message;
        this.code = code;
    }
}
