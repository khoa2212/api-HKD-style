package com.example.user_service.exception;

public class ExceptionMessage {
    public static final String USER_NOT_FOUND = "User doesn't exist";
    public static final String EMAIL_NOT_FOUND = "Email cannot be found";
    public static final String EMAIL_DUPLICATE = "Email is duplicated";
    public static final String INCORRECT_PASSWORD = "Current password is incorrect";
    public static final String NEW_PASSWORD_CONFIRM_MISMATCH = "New password and confirm password don't match";
    public static final String EXPIRED_URL_TOKEN = "The token is expired";
    public static final String INVALID_URL_TOKEN = "Invalid URL token";
}
