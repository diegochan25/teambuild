package org.typecrafters.teambuild.dto;

public record LoginRequest(
    String userNameOrEmail,
    String password,
    boolean rememberMe
) { }
