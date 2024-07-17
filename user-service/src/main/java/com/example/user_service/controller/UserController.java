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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

//Context path: /api/users
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Register new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Register new user successfully",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(
            path = "/",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody AddUserRequestDTO request) throws DataIntegrityViolationException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(request));
    }

    @Operation(summary = "Get user info by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return user info successfully",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))}
            ),
            @ApiResponse(responseCode = "404", description = "User cannot be found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(
            path = "/{userId}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("userId") String userId) throws UserNotFoundException {
        return ResponseEntity.ok(userService.findUserById(UUID.fromString(userId)));
    }

    @Operation(summary = "Update user info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update user info successfully",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "User cannot be found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(
            path = "/{userId}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<UserResponseDTO> updateUser(
            @Valid @RequestBody UpdateUserRequestDTO request, @PathVariable("userId") String userId) throws UserNotFoundException {
        return ResponseEntity.ok(userService.updateUserById(UUID.fromString(userId), request));
    }

    @Operation(summary = "Change user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Change password successfully",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "User cannot be found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(
            path = "/password",
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

    @Operation(summary = "Check email validity and send forgot password mail to user email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email is valid and mail is sent to user email",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GeneralResponseDTO.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "User cannot be found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(
            path = "/forgot-password",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<GeneralResponseDTO> forgotPassword(@RequestBody ForgotPasswordRequestDTO request) throws UserNotFoundException, NoSuchAlgorithmException {
        return ResponseEntity.ok(userService.forgotPassword(request));
    }

    @Operation(summary = "Update new password after forgot password request is sent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update password successfully",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GeneralResponseDTO.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "User cannot be found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(
            path = "/reset-password",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<GeneralResponseDTO> resetPassword(@RequestBody ResetPasswordRequestDTO request)
            throws UserNotFoundException, PasswordMismatchException, ExpiredURLTokenException, NoSuchAlgorithmException, InvalidURLTokenException {
        return ResponseEntity.ok(userService.resetPassword(request));
    }

    @Operation(summary = "Logout user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successfully",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "User cannot be found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(
            path = "/logout",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<GeneralResponseDTO> logout(@CookieValue("refreshToken") String token) {
        ResponseCookie cookie = userService.logout(token);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new GeneralResponseDTO("User has been successfully logout"));
    }

}
