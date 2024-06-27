package com.example.user_service.utils;

public class HeaderUtils {
    public static String extractTokenFromBearer(String authHeader) {
        return authHeader.substring("Bearer ".length());
    }
}
