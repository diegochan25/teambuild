package org.typecrafters.teambuild.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.typecrafters.teambuild.entity.Verification;

public interface VerificationRepository extends JpaRepository<Verification, Long> {

} 