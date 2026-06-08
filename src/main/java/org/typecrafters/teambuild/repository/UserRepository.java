package org.typecrafters.teambuild.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.typecrafters.teambuild.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserNameOrEmail(String userName, String email);
}
