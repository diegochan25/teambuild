package org.typecrafters.teambuild.controller;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.typecrafters.teambuild.domain.enums.AppStatusCode;
import org.typecrafters.teambuild.domain.exception.AppException;
import org.typecrafters.teambuild.dto.LoginRequest;
import org.typecrafters.teambuild.dto.SignupRequest;
import org.typecrafters.teambuild.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("login")
    public void login(@RequestBody LoginRequest body, HttpServletRequest request, HttpServletResponse response) {
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        try {
            String jsessionid = authService.authenticateUser(
                body.email(), 
                body.password(), 
                body.rememberMe(),
                ipAddress,
                userAgent
            );

            Duration maxAge = Duration.ofDays(body.rememberMe() ? 30 : 7);

            ResponseCookie cookie = ResponseCookie.from("JSESSIONID", jsessionid)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .path("/")
                .maxAge(maxAge)
                .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        } catch (AppException e) {
            throw new ResponseStatusException(AppException.toHttpStatus(e.getCode()), e.getMessage());
        }

    }

    @PostMapping("signup")
    public void signup(SignupRequest body) { // User

    }

    @PostMapping("logout")
    public void logout(HttpServletRequest request) {
        String jsessionid = request.getHeader("Authorization").split(" ", 1)[1];
        try {
            authService.revokeSession(jsessionid);
            return;
        } catch (Exception e) {
            throw new ResponseStatusException(AppException.toHttpStatus(AppStatusCode.INTERNAL_SERVER_ERROR), e.getMessage());
        }
    }
}
