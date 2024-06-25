package com.example.apidemo.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessage {
    public static final String PRODUCT_NOT_FOUND = "Product cannot be found";
    public static final String PRODUCT_NOT_FOUND_CODE = "ProductNotFound";
}
