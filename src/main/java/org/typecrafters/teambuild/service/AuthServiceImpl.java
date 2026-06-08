package org.typecrafters.teambuild.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.typecrafters.teambuild.domain.exception.ApplicationException;
import org.typecrafters.teambuild.domain.exception.ClientException;
import org.typecrafters.teambuild.domain.util.TokenGenerator;
import org.typecrafters.teambuild.entity.Session;
import org.typecrafters.teambuild.entity.User;
import org.typecrafters.teambuild.repository.SessionRepository;
import org.typecrafters.teambuild.repository.UserRepository;

public class AuthServiceImpl implements AuthService {
    private static final String FAKE_HASH = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
    private final Duration standardSession = Duration.ofDays(7);
    private final Duration extendedSession = Duration.ofDays(30);
    private final int tokenLength = 32;

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(
            UserRepository userRepository,
            SessionRepository sessionRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String authenticateUser(
            String userNameOrEmail,
            String password,
            boolean rememberMe,
            String ipAddress,
            String userAgent) {
        ClientException unauthorized = new ClientException("Unauthorized.");

        User user = userRepository.findByUserNameOrEmail(userNameOrEmail, userNameOrEmail)
                .orElse(null);

        if (user == null) {
            passwordEncoder.matches(password, FAKE_HASH);
            throw unauthorized;
        }

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw unauthorized;
        }

        try {
            Session session = new Session();

            Instant now = Instant.now();

            session.setUser(user);
            session.setJsessionid(TokenGenerator.randomBase64Url(tokenLength));
            session.setIpAddress(InetAddress.getByName(ipAddress));
            session.setUserAgent(userAgent);
            session.setIssuedAt(now);
            session.setExpiresAt(now.plus(rememberMe ? extendedSession : standardSession));

            Session result = sessionRepository.save(session);
            return result.getJsessionid();
        } catch (UnknownHostException e) {
            throw new ApplicationException(
                "An unexpected error occurred while transforming the request's IP address to an InetAddress object.",
                e
            );
        }
    }

    @Transactional
    @Override
    public void revokeSession(String jsessionid) {
        Session session = sessionRepository.findByJsessionid(jsessionid)
                .orElse(null);

        if (session != null && session.getRevokedAt() == null) {
            Instant now = Instant.now();

            session.setRevokedAt(now);
            session.setExpiresAt(now);
        }
    }
}