package com.example.user_service.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PublisherConst {
    public static final String USER_EXCHANGE = "user";
    public static final String FORGOT_PASSWORD_EVENT = "user.forgot-password";
}
