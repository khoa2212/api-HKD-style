package com.example.demo.service;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.dto.AuthRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Getter
    private final int accessTokenExpTime = 60;
    @Getter
    private final int refreshTokenExpTime = 5*60;

    @Autowired
    public AuthService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public Map<String, String> authenticate(AuthRequest request) throws BadCredentialsException, JWTCreationException {
        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()
        );
        Authentication userInfo = authenticationManager.authenticate(userToken);
        User user = (User) userInfo.getPrincipal();
//        String email = request.getEmail();
//        String password = request.getPassword();
//        User user = userRepository
//                .findByEmail(email)
//                .orElseThrow(() -> new AuthenticateException(ExceptionMessage.FAILED_AUTHENTICATION_MESSAGE));
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            throw new AuthenticateException(ExceptionMessage.FAILED_AUTHENTICATION_MESSAGE);
        Map<String, String> claims = new HashMap<>();
        String userId = user.getId().toString();
        claims.put("email", user.getEmail());
        claims.put("role", userInfo.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", ")));
        String accessToken = jwtService.generateToken(userId, accessTokenExpTime, claims);
        String refreshToken = jwtService.generateToken(userId, refreshTokenExpTime, claims);

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
}
