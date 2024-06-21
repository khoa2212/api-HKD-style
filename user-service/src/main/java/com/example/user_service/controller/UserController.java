package com.example.user_service.controller;

import com.example.user_service.dto.AddUserRequestDTO;
import com.example.user_service.dto.UpdateUserRequestDTO;
import com.example.user_service.dto.UserResponseDTO;
import com.example.user_service.exception.UserNotFoundException;
import com.example.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(
            path = "/api/users",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody AddUserRequestDTO request) throws DataIntegrityViolationException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(request));
    }

    @GetMapping(
            path = "/api/users/{userId}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("userId") String userId) throws UserNotFoundException {
        return ResponseEntity.ok(userService.findUserById(UUID.fromString(userId)));
    }

    @PutMapping(
            path = "/api/users/{userId}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<UserResponseDTO> updateUser(
            @Valid @RequestBody UpdateUserRequestDTO request, @PathVariable("userId") String userId) throws UserNotFoundException {
        return ResponseEntity.ok(userService.updateUserById(UUID.fromString(userId), request));
    }
}
