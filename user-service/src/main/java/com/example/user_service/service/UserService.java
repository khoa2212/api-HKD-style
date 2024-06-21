package com.example.user_service.service;

import com.example.user_service.dto.AddUserRequestDTO;
import com.example.user_service.dto.UpdateUserRequestDTO;
import com.example.user_service.dto.UserResponseDTO;
import com.example.user_service.entity.Role;
import com.example.user_service.entity.User;
import com.example.user_service.exception.ExceptionMessage;
import com.example.user_service.exception.UserNotFoundException;
import com.example.user_service.mapper.UserMapper;
import com.example.user_service.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public String sendHelloWorld() {
        return "Hello world";
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
}
