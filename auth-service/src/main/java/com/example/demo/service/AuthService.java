package com.example.demo.service;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.dto.AuthRequest;
import com.example.demo.entity.User;
import com.example.demo.exception.AuthenticateException;
import com.example.demo.exception.ExceptionMessage;
import com.example.demo.repository.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    @Getter
    private final int accessTokenExpTime = 30 * 60;
    @Getter
    private final int refreshTokenExpTime = 3 * 24 * 60 * 60;

    @Autowired
    public AuthService(UserRepository userRepository, JWTService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Map<String, String> authenticate(AuthRequest request) throws BadCredentialsException, JWTCreationException, AuthenticateException {
        String email = request.getEmail();
        String password = request.getPassword();
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new AuthenticateException(ExceptionMessage.FAILED_AUTHENTICATION_MESSAGE));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticateException(ExceptionMessage.FAILED_AUTHENTICATION_MESSAGE);
        }
        Map<String, String> claims = new HashMap<>();
        String userId = user.getId().toString();
        claims.put("email", user.getEmail());
        claims.put("role", user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", ")));
        return getAccessAndRefreshToken(userId, claims);
    }

    public Map<String, String> getAccessAndRefreshToken(String subject, Map<String, String> claims) {
        String accessToken = jwtService.generateToken(subject, accessTokenExpTime, claims);
        String refreshToken = jwtService.generateToken(subject, refreshTokenExpTime, claims);
        return new HashMap<>(Map.of("accessToken", accessToken, "refreshToken", refreshToken));
    }

    public String getNewAccessToken(String refreshToken) throws JWTVerificationException, JWTCreationException {
        Map<String, String> payload = jwtService.validateToken(refreshToken);
        Map<String, String> claims = new HashMap<>();
        payload.keySet().stream()
        .filter(k -> k.equals("email") || k.equals("role") || k.equals("sub"))
        .forEach(k -> {
            claims.put(k, payload.get(k));
        });
        return jwtService.generateToken(claims.get("sub"), accessTokenExpTime, claims);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User addOAuth2User(User user) {
        return userRepository.save(user);
    }
}
