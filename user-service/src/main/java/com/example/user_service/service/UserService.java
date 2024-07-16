package com.example.user_service.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.user_service.constant.EmailMessage;
import com.example.user_service.dto.AddUserRequestDTO;
import com.example.user_service.dto.ChangePasswordRequestDTO;
import com.example.user_service.dto.ForgotPasswordRequestDTO;
import com.example.user_service.dto.GeneralResponseDTO;
import com.example.user_service.dto.ResetPasswordRequestDTO;
import com.example.user_service.dto.SendEmailRequestDTO;
import com.example.user_service.dto.UpdateUserRequestDTO;
import com.example.user_service.dto.UserResponseDTO;
import com.example.user_service.entity.Role;
import com.example.user_service.entity.URLToken;
import com.example.user_service.entity.User;
import com.example.user_service.exception.ExceptionMessage;
import com.example.user_service.exception.ExpiredURLTokenException;
import com.example.user_service.exception.InvalidURLTokenException;
import com.example.user_service.exception.PasswordMismatchException;
import com.example.user_service.exception.SendMailException;
import com.example.user_service.exception.UserNotFoundException;
import com.example.user_service.mapper.UserMapper;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.utils.JWTService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JWTService jwtService;
    private final URLTokenService urlTokenService;
    private final String frontEndResetPasswordEndpoint;
    private final MessageProducerService messageProducerService;
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper,
            JWTService jwtService,
            URLTokenService urlTokenService,
            @Value("${FRONTEND_RESET_PASSWORD_ENDPOINT}") String frontEndResetPasswordEndpoint,
            MessageProducerService messageProducerService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.urlTokenService = urlTokenService;
        this.frontEndResetPasswordEndpoint = frontEndResetPasswordEndpoint;
        this.messageProducerService = messageProducerService;
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

    public GeneralResponseDTO forgotPassword(ForgotPasswordRequestDTO request) throws UserNotFoundException, NoSuchAlgorithmException {
        String email = request.getEmail();
        String urlToken = urlTokenService.addPasswordResetURLToken(email);
        SendEmailRequestDTO sendMailRequest = SendEmailRequestDTO.builder()
                .recipientEmail(email)
                .subject(EmailMessage.PASSWORD_RESET_SUBJECT)
                .content(String.format(EmailMessage.PASSWORD_RESET_CONTENT, frontEndResetPasswordEndpoint, urlToken))
                .build();

        messageProducerService.sendForgotPasswordMessage(sendMailRequest);


//        RestClient client = RestClient.create("http://email-service:8085");
//        client.post()
//                .uri("/api/mail")
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(sendMailRequest)
//                .retrieve()
//                .onStatus(HttpStatusCode::isError, (req, res) -> {
//                    throw new SendMailException("Failed to send mail");
//                })
//                .body(GeneralResponseDTO.class);

        return new GeneralResponseDTO("A password reset url has been sent to user's email");

    }

    public GeneralResponseDTO resetPassword(ResetPasswordRequestDTO request)
            throws JWTVerificationException, UserNotFoundException, ExpiredURLTokenException, NoSuchAlgorithmException, PasswordMismatchException, InvalidURLTokenException {
        URLToken urlToken = urlTokenService.validatePasswordResetURLToken(request.getEmail(), request.getToken());
        if (!request.getNewPassword().equals(request.getNewPasswordConfirm()))
            throw new PasswordMismatchException(ExceptionMessage.NEW_PASSWORD_CONFIRM_MISMATCH);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessage.USER_NOT_FOUND));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        urlTokenService.removeValidatedURLToken(urlToken);
        return new GeneralResponseDTO("Password has been changed successfully");

    }
}
