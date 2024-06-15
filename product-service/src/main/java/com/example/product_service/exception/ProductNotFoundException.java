package com.example.product_service.exception;

import lombok.Getter;

@Getter
public class ProductNotFoundException extends Exception {
    private String message;
    private String code;

    public ProductNotFoundException(String message, String code) {
        this.message = message;
        this.code = code;
    }
}
