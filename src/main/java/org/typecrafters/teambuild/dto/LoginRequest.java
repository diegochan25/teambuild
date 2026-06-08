package org.typecrafters.teambuild.dto;

public record LoginRequest(
    String email,
    String password,
    boolean rememberMe
) { }
