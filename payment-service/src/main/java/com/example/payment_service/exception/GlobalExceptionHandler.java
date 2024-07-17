package com.example.payment_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({FailedTransactionException.class})
    public ResponseEntity<ErrorBody> handleFailedTransactionException(
            FailedTransactionException ex, WebRequest request
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getErrorBody());
    }
}
