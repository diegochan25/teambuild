package org.typecrafters.teambuild.repository;

import java.time.Instant;
import java.util.Optional;

import org.typecrafters.teambuild.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.typecrafters.teambuild.entity.Session;

import jakarta.transaction.Transactional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByJsessionid(String jsessionid);
    @Transactional void deleteAllByUser(User user);

    @Transactional void deleteAllByExpiresAtBeforeOrRevokedAtBefore(Instant expiresAt, Instant revokedAt);
}
