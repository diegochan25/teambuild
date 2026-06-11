package org.typecrafters.teambuild.dto;

public record VerifyEmailRequest(
    String email,
    int code
) { }