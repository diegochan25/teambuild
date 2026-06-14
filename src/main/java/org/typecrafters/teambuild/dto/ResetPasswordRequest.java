package org.typecrafters.teambuild.dto;

public record ResetPasswordRequest(
    String email,
    String code,
    String password,
    String confirmPassword
) { }