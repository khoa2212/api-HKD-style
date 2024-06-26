package com.example.user_service.controller;

import com.example.user_service.dto.AddUserRequestDTO;
import com.example.user_service.dto.ChangePasswordRequestDTO;
import com.example.user_service.dto.ForgotPasswordRequestDTO;
import com.example.user_service.dto.GeneralResponseDTO;
import com.example.user_service.dto.ResetPasswordRequestDTO;
import com.example.user_service.dto.UpdateUserRequestDTO;
import com.example.user_service.dto.UserResponseDTO;
import com.example.user_service.exception.ExpiredURLTokenException;
import com.example.user_service.exception.InvalidURLTokenException;
import com.example.user_service.exception.PasswordMismatchException;
import com.example.user_service.exception.UserNotFoundException;
import com.example.user_service.service.UserService;
import com.example.user_service.utils.HeaderUtils;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(
            path = "/users",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody AddUserRequestDTO request) throws DataIntegrityViolationException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(request));
    }

    @GetMapping(
            path = "/users/{userId}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("userId") String userId) throws UserNotFoundException {
        return ResponseEntity.ok(userService.findUserById(UUID.fromString(userId)));
    }

    @PutMapping(
            path = "/users/{userId}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<UserResponseDTO> updateUser(
            @Valid @RequestBody UpdateUserRequestDTO request, @PathVariable("userId") String userId) throws UserNotFoundException {
        return ResponseEntity.ok(userService.updateUserById(UUID.fromString(userId), request));
    }

    @PutMapping(
            path = "/users/password",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<GeneralResponseDTO> changePassword(
            @Valid @RequestBody ChangePasswordRequestDTO request,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @CookieValue("refreshToken") String refreshToken
    ) throws UserNotFoundException, PasswordMismatchException {
        String accessToken = HeaderUtils.extractTokenFromBearer(authHeader);
        GeneralResponseDTO res = userService.changePassword(request, accessToken);
        ResponseCookie cookie = userService.logout(refreshToken);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(res);
    }

    @PostMapping(
            path = "/users/forgot-password",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<GeneralResponseDTO> forgotPassword(@RequestBody ForgotPasswordRequestDTO request) throws UserNotFoundException, NoSuchAlgorithmException {
        return ResponseEntity.ok(userService.forgotPassword(request));
    }

    @PostMapping(
            path = "/users/reset-password",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<GeneralResponseDTO> resetPassword(@RequestBody ResetPasswordRequestDTO request)
            throws UserNotFoundException, PasswordMismatchException, ExpiredURLTokenException, NoSuchAlgorithmException, InvalidURLTokenException {
        return ResponseEntity.ok(userService.resetPassword(request));
    }


    @PostMapping(
            path = "/users/logout",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<GeneralResponseDTO> logout(@CookieValue("refreshToken") String token) {
        ResponseCookie cookie = userService.logout(token);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new GeneralResponseDTO("User has been successfully logout"));
    }

}
