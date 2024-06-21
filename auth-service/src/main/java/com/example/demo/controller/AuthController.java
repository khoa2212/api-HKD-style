package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.exception.AuthenticateException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthController {
    private AuthService authService;

    @Autowired
    public AuthController(AuthService authSerVice) {
        this.authService = authSerVice;
    }

    @PostMapping("/api/auth")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) throws AuthenticateException {
        return ResponseEntity.ok().body(authService.authenticate(request));
    }

}
