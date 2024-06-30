package com.example.user_service.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

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

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> handleConstraintValidationException(
            ConstraintViolationException ex, WebRequest request
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorMessage(ex.getMessage(), "BadRequest"));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        FieldError err = ex.getBindingResult().getFieldError();
        String message = "";
        if (Optional.ofNullable(err).isPresent())
            message = err.getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorMessage(message, "BadRequest")
        );
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorMessage> handlePasswordMismatchException(
            PasswordMismatchException ex, WebRequest request
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorMessage(ex.getMessage(), "BadRequest")
        );
    }

    @ExceptionHandler({InvalidURLTokenException.class, ExpiredURLTokenException.class})
    public ResponseEntity<ErrorMessage> handleURLTokenException(
            BaseAppException ex, WebRequest request
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getErrorMessage());
    }
}
