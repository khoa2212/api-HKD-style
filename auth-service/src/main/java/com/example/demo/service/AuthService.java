package com.example.demo.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.AuthenticateException;
import com.example.demo.exception.ExceptionMessage;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Autowired
    public AuthService(
            UserRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse authenticate(AuthRequest request) throws BadCredentialsException, JWTCreationException {
        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()
        );
        Authentication userInfo = authenticationManager.authenticate(userToken);

//        String email = request.getEmail();
//        String password = request.getPassword();
//        User user = userRepository
//                .findByEmail(email)
//                .orElseThrow(() -> new AuthenticateException(ExceptionMessage.FAILED_AUTHENTICATION_MESSAGE));
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            throw new AuthenticateException(ExceptionMessage.FAILED_AUTHENTICATION_MESSAGE);
        Map<String, String> claims = new HashMap<>();
        claims.put("email", ((User) userInfo.getPrincipal()).getEmail());
        claims.put("role", userInfo.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", ")));
        String token = jwtService.generateToken(claims);
        return new AuthResponse(token);
//        try {
//            Algorithm algorithm = Algorithm.HMAC256(System.getenv("SECRET_KEY"));
//            String token = JWT.create()
//                    .withSubject(userInfo.getPrincipal().toString())
//                    .withExpiresAt(Instant.now().plusSeconds(3 * 60 * 60))
//                    .sign(algorithm);
//            return new AuthResponse(token);
//        }
//        catch (JWTCreationException e) {
//            throw new RuntimeException("Failed to generate token");
//        }
    }

    public String register(AuthRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        return "Added new user successfully";
    }

    public UserResponseDTO findUserById(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User doesn't exist"));
        return UserResponseDTO.builder()
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .build();
    }
}
