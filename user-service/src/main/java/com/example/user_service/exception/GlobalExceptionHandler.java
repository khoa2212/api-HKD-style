package com.example.user_service.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleUserNotFoundException(
            UserNotFoundException ex, WebRequest request
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(ex.getMessage(), "UserNotFound"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> handleDataConstraintViolationException(
            DataIntegrityViolationException ex, WebRequest request
    ) {
        if (ex.getMessage().contains("users_email_key"))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorMessage("Email is already existed", "DuplicateEmail"));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorMessage("Email cannot be empty", "EmptyEmail"));
    }
}
