package org.typecrafters.teambuild.controller;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.typecrafters.teambuild.domain.exception.ApplicationException;
import org.typecrafters.teambuild.domain.exception.ClientException;
import org.typecrafters.teambuild.dto.LoginRequest;
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
                body.userNameOrEmail(), 
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
        } catch (ClientException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (ApplicationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @PostMapping("logout")
    public void logout(HttpServletRequest request) {
        String jsessionid = request.getHeader("Authorization").split(" ", 1)[1];
        
    }
}
