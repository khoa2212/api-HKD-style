package com.example.demo.handler;

import com.example.demo.dto.AuthResponse;
import com.example.demo.entity.ExternalProvider;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.ExceptionMessage;
import com.example.demo.exception.OAuthEmailConflictException;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.JWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final AuthService authService;

    public OAuth2SuccessHandler(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultOidcUser principal = (DefaultOidcUser) authentication.getPrincipal();
        ObjectMapper mapper = new ObjectMapper();
        try {
            User user = retrieveUserFromOAuthPrincipal(principal);

            Map<String, String> claims = new HashMap<>();
            claims.put("email", user.getEmail());
            claims.put("role", user.getRole().toString());
            Map<String, String> tokens = authService.getAccessAndRefreshToken(user.getId().toString(), claims);
            String accessToken = tokens.get("accessToken");
            String refreshToken = tokens.get("refreshToken");

            AuthResponse authResponse = new AuthResponse(accessToken);
            ResponseCookie cookie = ResponseCookie.from("refreshToken")
                    .httpOnly(true)
                    .maxAge(authService.getRefreshTokenExpTime())
                    .value(refreshToken)
                    .build();

            String responseBody = mapper.writeValueAsString(authResponse);
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            response.getWriter().write(responseBody);
        }
        catch (OAuthEmailConflictException ex) {
            response.setStatus(HttpStatus.CONFLICT.value());
            response.getWriter().write(mapper.writeValueAsString(ex.getErrorBody()));
        }
    }

    private User retrieveUserFromOAuthPrincipal(OidcUser oidcUser) {
        OidcUserInfo userInfo = oidcUser.getUserInfo();
        String email = userInfo.getEmail();
        User authenticatedUser;
        Optional<User> checkUser = authService.findUserByEmail(email);
        if (checkUser.isEmpty()) {
            authenticatedUser = authService.addOAuth2User(User.builder()
                    .email(email)
                    .role(Role.USER)
                    .fullName(userInfo.getFullName())
                    .externalProvider(ExternalProvider.GOOGLE)
                    .build());
        }
        else if (checkUser.get().getExternalProvider() == ExternalProvider.GOOGLE) {
            authenticatedUser = checkUser.get();
        }
        else
            throw new OAuthEmailConflictException(ExceptionMessage.OAUTH_EMAIL_CONFLICT);

        return authenticatedUser;
    }
}
