package org.typecrafters.teambuild.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.typecrafters.teambuild.entity.User;

import jakarta.transaction.Transactional;

import org.typecrafters.teambuild.entity.Token;
import org.typecrafters.teambuild.domain.enums.TokenType;



public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByUserAndCodeHashAndType(User user, String codeHash, TokenType type);
    @Transactional void deleteAllByUserAndType(User user, TokenType type);
} 