package com.example.user_service.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.user_service.dto.AddUserRequestDTO;
import com.example.user_service.dto.ChangePasswordRequestDTO;
import com.example.user_service.dto.GeneralResponseDTO;
import com.example.user_service.dto.UpdateUserRequestDTO;
import com.example.user_service.dto.UserResponseDTO;
import com.example.user_service.entity.Role;
import com.example.user_service.entity.User;
import com.example.user_service.exception.ExceptionMessage;
import com.example.user_service.exception.PasswordMismatchException;
import com.example.user_service.exception.UserNotFoundException;
import com.example.user_service.mapper.UserMapper;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.utils.JWTService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JWTService jwtService;
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper,
            JWTService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }

    public UserResponseDTO addUser(AddUserRequestDTO request) throws DataIntegrityViolationException {
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .fullName(request.getFullName())
                .build();
        return userMapper.toUserResponseDTO(userRepository.save(user));
    }

    public UserResponseDTO findUserById(UUID userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(ExceptionMessage.USER_NOT_FOUND));
        return userMapper.toUserResponseDTO(user);
    }

    public UserResponseDTO updateUserById(UUID userId, UpdateUserRequestDTO request) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(ExceptionMessage.USER_NOT_FOUND));
        if (request.getAddress() != null)
            user.setAddress(request.getAddress());
        if (request.getFullName() != null)
            user.setFullName(request.getFullName());
        if(request.getCity() != null)
            user.setCity(request.getCity());
        if (request.getPhoneNumber() != null)
            user.setPhoneNumber(request.getPhoneNumber());
        return userMapper.toUserResponseDTO(userRepository.save(user));
    }

    public GeneralResponseDTO changePassword(ChangePasswordRequestDTO request, String accessToken)
            throws JWTVerificationException, UserNotFoundException, PasswordMismatchException {
        String email = jwtService.ExtractEmailFromToken(accessToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessage.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword()))
            throw new PasswordMismatchException(ExceptionMessage.INCORRECT_PASSWORD);
        if (!request.getNewPassword().equals(request.getNewPasswordConfirm()))
            throw new PasswordMismatchException(ExceptionMessage.NEW_PASSWORD_CONFIRM_MISMATCH);

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return new GeneralResponseDTO("Changed password successfully");
    }

    public ResponseCookie logout(String token) throws JWTVerificationException {
        Map<String, String> payload = jwtService.validateToken(token);
        SecurityContextHolder.getContext().setAuthentication(null);
        return ResponseCookie.from("refreshToken")
                .maxAge(0)
                .httpOnly(true)
                .value(null)
                .build();
    }
}
