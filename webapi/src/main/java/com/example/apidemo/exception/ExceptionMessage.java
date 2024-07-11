package com.example.apidemo.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessage {
    public static final String PRODUCT_NOT_FOUND = "Product cannot be found";
    public static final String REVIEW_NOT_FOUND = "Review cannot be found";
    public static final String PRODUCT_NOT_FOUND_CODE = "productNotFound";
    public static final String REVIEW_NOT_FOUND_CODE = "reviewNotFound";
    public static final String SUCCESS_MESSAGE = "Success";
}
