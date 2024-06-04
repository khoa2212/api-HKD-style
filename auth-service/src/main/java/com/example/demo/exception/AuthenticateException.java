package com.example.demo.exception;

public class AuthenticateException extends Exception {
    private String message;

    public AuthenticateException(String message) {
        super(message);
    }
}
