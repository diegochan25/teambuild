package org.typecrafters.teambuild.service;

import org.typecrafters.teambuild.entity.User;

public interface AuthService {
    public String authenticateUser(
        String email, 
        String password,
        boolean rememberMe, 
        String ipAddress,
        String userAgent
    );

    public void verifyEmailAddress(
        String email,
        String code
    );

    public User createAccount(
        String firstName, 
        String lastName,
        String email, 
        String password, 
        String confirmPassword,
        boolean newsletterOptIn
    );

    public void revokeSession(String jsessionid);
}