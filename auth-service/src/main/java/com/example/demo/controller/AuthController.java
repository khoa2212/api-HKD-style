package com.example.demo.controller;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.exception.AuthenticateException;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authSerVice) {
        this.authService = authSerVice;
    }

    @PostMapping("/api/auth")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) throws BadCredentialsException, JWTCreationException, AuthenticateException {
        Map<String, String> tokens = authService.authenticate(request);
        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");

        ResponseCookie cookie = ResponseCookie.from("refreshToken")
                .value(refreshToken)
                .maxAge(authService.getRefreshTokenExpTime())
                .httpOnly(true)
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().headers(headers).body(new AuthResponse(accessToken));
    }

    @GetMapping("/api/auth/token")
    public ResponseEntity<AuthResponse> getNewAccessToken(
            @CookieValue("refreshToken") String refreshToken) throws JWTVerificationException, JWTCreationException {

        return ResponseEntity.ok().body(new AuthResponse(authService.getNewAccessToken(refreshToken)));
    }

}
