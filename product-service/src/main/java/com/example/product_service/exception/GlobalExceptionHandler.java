package com.example.product_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorBody> userNotFoundExceptionHandler(
            ProductNotFoundException ex, WebRequest request
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorBody(ex.getMessage(), ex.getCode()));
    }
}
