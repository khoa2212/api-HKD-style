package com.example.demo.exception;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorBody> authenticateExceptionHandler(
            AuthenticationException ex, WebRequest request
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorBody(ExceptionMessage.FAILED_AUTHENTICATION_MESSAGE, "BadRequest"));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorBody> userNotFoundExceptionHandler(
            UserNotFoundException ex, WebRequest request
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getErrorBody());
    }

    @ExceptionHandler(JWTCreationException.class)
    public ResponseEntity<ErrorBody> JWTCreationExceptionHandler(
            JWTCreationException ex, WebRequest request
    ) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorBody("Failed to generate JWT token", "InternalError"));
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ErrorBody> JWTVerifyExceptionHandler(
            JWTVerificationException ex, WebRequest request
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorBody("Invalid token", "Unauthorized"));
    }

    @ExceptionHandler(OAuthEmailConflictException.class)
    public ResponseEntity<ErrorBody> oauthEmailConflictExceptionHandler(
            OAuthEmailConflictException ex, WebRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getErrorBody());
    }

}
