package org.typecrafters.teambuild.dto;

public record VerifyCodeRequest(
    String email,
    String code
) { }