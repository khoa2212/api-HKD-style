package com.example.apidemo.exception;

import lombok.Getter;

@Getter
public class ItemNotFoundException extends Exception {
    private String message;
    private String code;

    public ItemNotFoundException(String message, String code) {
        this.message = message;
        this.code = code;
    }
}
