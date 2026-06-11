package org.typecrafters.teambuild.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.typecrafters.teambuild.entity.Verification;

public interface VerificationRepository extends JpaRepository<Verification, Long> {
    Optional<Verification> findByEmailAndCodeHash(String email, String codeHash);
} 