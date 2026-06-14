package org.typecrafters.teambuild.controller;

import java.time.Duration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.typecrafters.teambuild.domain.exception.AppException;
import org.typecrafters.teambuild.dto.ForgotPasswordRequest;
import org.typecrafters.teambuild.dto.LoginRequest;
import org.typecrafters.teambuild.dto.ResetPasswordRequest;
import org.typecrafters.teambuild.dto.SignupRequest;
import org.typecrafters.teambuild.dto.UserResponse;
import org.typecrafters.teambuild.dto.VerifyCodeRequest;
import org.typecrafters.teambuild.entity.User;
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
    public void login(
        @RequestBody LoginRequest body,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
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
            throw e.toHttpException();
        }

    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("signup")
    public UserResponse signup(@RequestBody SignupRequest body) {
        try {
            User user = authService.createAccount(
                body.firstName(),
                body.lastName(),
                body.email(),
                body.password(),
                body.confirmPassword(),
                body.newsletterOptIn()
            );
            return UserResponse.fromUser(user);
        } catch (AppException e) {
            throw e.toHttpException();
        }
    }

    @PostMapping("email/verify")
    public void verifyEmailAddress(@RequestBody VerifyCodeRequest body) {
        try {
            authService.verifyEmailAddress(body.email(), body.code());
        } catch (AppException e) {
            throw e.toHttpException();
        }
    }

    @PostMapping("password/forgot")
    public void requestPasswordReset(@RequestBody ForgotPasswordRequest body) {
        try {
            authService.sendPasswordResetCode(body.email());
        } catch (AppException e) {
            throw e.toHttpException();
        }
    }

    @PostMapping("password/verify")
    public void verifyPasswordReset(@RequestBody VerifyCodeRequest body) {
        try {
            authService.verifyPasswordResetCode(body.email(), body.code());
        } catch (AppException e) {
            throw e.toHttpException();
        }
    }

    @PostMapping("password/reset")
    public void resetUserPassword(@RequestBody ResetPasswordRequest body) {
        try {
            authService
                .updatePassword(body.email(), body.code(), body.password(), body.confirmPassword());
        } catch (AppException e) {
            throw e.toHttpException();
        }
    }

    @PostMapping("logout")
    public void logout(
        @CookieValue(name = "JSESSIONID", required = false) String jsessionid,
        HttpServletResponse response) {
        try {
            if (jsessionid != null) {
                authService.revokeSession(jsessionid);
            }
            ResponseCookie cookie = ResponseCookie
                .from("JSESSIONID", jsessionid != null ? jsessionid : "").httpOnly(true)
                .sameSite("None").secure(true).path("/").maxAge(0).build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        } catch (AppException e) {
            throw e.toHttpException();
        }
    }
}
