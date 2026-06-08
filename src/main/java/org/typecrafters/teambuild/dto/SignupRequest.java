package org.typecrafters.teambuild.dto;

public record SignupRequest(
    String firstName,
    String lastName,
    String email,
    String password,
    String confirmPassword,
    boolean newsletterOptIn
) { }