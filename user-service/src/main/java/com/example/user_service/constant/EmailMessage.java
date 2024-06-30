package com.example.user_service.constant;

import org.springframework.beans.factory.annotation.Value;

public class EmailMessage {
    public static final String PASSWORD_RESET_SUBJECT = "Password reset";
    public static final String PASSWORD_RESET_CONTENT = "Click this link to create a new password: %s?token=%s";
}
