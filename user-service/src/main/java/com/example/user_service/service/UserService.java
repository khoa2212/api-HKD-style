package com.example.user_service.service;

import com.example.user_service.dto.AddUserRequestDTO;
import com.example.user_service.dto.UserResponseDTO;
import com.example.user_service.entity.Role;
import com.example.user_service.entity.User;
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
                .displayName(request.getDisplayName())
                .build();
//        if (request.getAddress() != null) {
//            Address address = userMapper.toAddress(request.getAddress());
//
//            user.setAddress(Set.of(address));
//        }
        return userMapper.toUserResponseDTO(userRepository.save(user));
    }

    public UserResponseDTO findUserById(UUID userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User cannot be found"));
        return userMapper.toUserResponseDTO(user);
    }
}
