package com.example.user_service.service;

import com.example.user_service.entity.URLToken;
import com.example.user_service.entity.URLTokenType;
import com.example.user_service.entity.User;
import com.example.user_service.exception.ExceptionMessage;
import com.example.user_service.exception.ExpiredURLTokenException;
import com.example.user_service.exception.InvalidURLTokenException;
import com.example.user_service.exception.UserNotFoundException;
import com.example.user_service.repository.URLTokenRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.utils.CommonUtils;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class URLTokenService {
    private final URLTokenRepository urlTokenRepository;
    private final UserRepository userRepository;
    private final int urlTokenExpireTime = 15 * 60;

    public URLTokenService(URLTokenRepository urlTokenRepository, UserRepository userRepository) {
        this.urlTokenRepository = urlTokenRepository;
        this.userRepository = userRepository;
    }

    public String addPasswordResetURLToken(String email) throws NoSuchAlgorithmException, UserNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException(ExceptionMessage.USER_NOT_FOUND));
        String token = CommonUtils.generateRandomToken();
        String hashedToken = CommonUtils.hashStringWithSHA256(token);

        URLToken urlToken = URLToken.builder()
                .tokenValue(hashedToken)
                .tokenType(URLTokenType.PASSWORD_RESET)
                .expireAt(LocalDateTime.now().plusSeconds(urlTokenExpireTime))
                .user(user)
                .build();
        urlTokenRepository.save(urlToken);
        return token;
    }

    public URLToken validatePasswordResetURLToken(String email, String urlTokenInput)
            throws NoSuchAlgorithmException, ExpiredURLTokenException, InvalidURLTokenException {
        List<URLToken> urlTokens = urlTokenRepository.findByEmailOrderByCreatedAt(email, URLTokenType.PASSWORD_RESET);
        if (urlTokens.isEmpty())
            throw new InvalidURLTokenException(ExceptionMessage.INVALID_URL_TOKEN);

        URLToken urlToken = urlTokens.get(0);
        if (urlToken.getExpireAt().isBefore(LocalDateTime.now()))
            throw new ExpiredURLTokenException(ExceptionMessage.EXPIRED_URL_TOKEN);
        String hashedURLToken = CommonUtils.hashStringWithSHA256(urlTokenInput);
        if (!hashedURLToken.equals(urlToken.getTokenValue()))
            throw new InvalidURLTokenException(ExceptionMessage.INVALID_URL_TOKEN);

        return urlToken;
    }

    public void removeValidatedURLToken(URLToken urlToken) {
        urlTokenRepository.delete(urlToken);
    }
}
