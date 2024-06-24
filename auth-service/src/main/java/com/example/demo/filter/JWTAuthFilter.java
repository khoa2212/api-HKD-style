package com.example.demo.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.exception.ErrorBody;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.JWTService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class JWTAuthFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final UserRepository userRepository;

    public JWTAuthFilter(JWTService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            ErrorBody body = new ErrorBody("", "Unauthorized");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(objectToJsonString(body));
            return;
        }
        try {
            String token = header.substring("Bearer ".length());
            Map<String, String> payload = jwtService.validateToken(token);
            UserDetails user = userRepository.findByEmail(payload.get("email")).orElse(null);
            if (user != null) {
                UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities()
                );
                userToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(userToken);
            }
            filterChain.doFilter(request, response);
        }
        catch (JWTVerificationException e) {
            ErrorBody body = new ErrorBody("Invalid token", "Unauthorized");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(objectToJsonString(body));
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        RequestMatcher matcher = new AntPathRequestMatcher(
                "/api/auth", HttpMethod.POST.name()
        );
        return matcher.matches(request);
    }

    private String objectToJsonString(Object object) throws JsonProcessingException {
        if (object == null)
            return null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        return mapper.writeValueAsString(object);
    }
}
