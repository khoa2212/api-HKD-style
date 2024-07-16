package com.example.apidemo.filter;

import com.example.apidemo.util.JWTService;
import com.example.apidemo.util.Role;
import com.example.apidemo.util.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JWTFilter extends OncePerRequestFilter {
    private final JWTService jwtService;

    public JWTFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring("Bearer ".length());
        Map<String, String> payload = jwtService.validateToken(token);
        String email = payload.get("email");
        String role = payload.get("role");
        String id = payload.get("sub");
        if (email != null) {
            User authUser = new User(UUID.fromString(id), email, Role.valueOf(role));
            UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                    authUser, null, List.of(new SimpleGrantedAuthority(role))
            );
            userToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(userToken);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().equals("/api/users") && request.getMethod().equals("POST");
    }
}
