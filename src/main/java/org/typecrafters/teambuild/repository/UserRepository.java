package org.typecrafters.teambuild.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.typecrafters.teambuild.domain.enums.UserStatus;
import org.typecrafters.teambuild.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndStatus(String email, UserStatus status);
}
