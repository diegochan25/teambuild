package org.typecrafters.teambuild.service;

public interface AuthService {
    public String authenticateUser(
        String userNameOrEmail, 
        String password,
        boolean rememberMe, 
        String ipAddress,
        String userAgent
    );

    public void revokeSession(String jsessionid);

}