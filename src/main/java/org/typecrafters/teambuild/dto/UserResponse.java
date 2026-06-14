package org.typecrafters.teambuild.dto;

import java.time.Instant;

import org.typecrafters.teambuild.domain.enums.UserStatus;
import org.typecrafters.teambuild.entity.User;

public record UserResponse(
    Long id,
    String firstName,
    String lastName,
    String email,
    boolean isPasswordSet,
    boolean newsletterOptIn,
    UserStatus status,
    Instant createdAt,
    Instant lastLoginAt,
    Instant updatedAt,
    Instant deletedAt
) {
    public static UserResponse fromUser(User user) {
        return new UserResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPasswordHash() != null,
            user.isNewsletterOptIn(),
            user.getStatus(),
            user.getCreatedAt(),
            user.getLastLoginAt(),
            user.getUpdatedAt(),
            user.getDeletedAt()
        );
    }
}