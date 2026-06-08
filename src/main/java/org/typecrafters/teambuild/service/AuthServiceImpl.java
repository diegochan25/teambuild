package org.typecrafters.teambuild.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.typecrafters.teambuild.domain.enums.UserStatus;
import org.typecrafters.teambuild.domain.exception.AppException;
import org.typecrafters.teambuild.domain.util.TokenGenerator;
import org.typecrafters.teambuild.entity.Session;
import org.typecrafters.teambuild.entity.User;
import org.typecrafters.teambuild.entity.UserProfile;
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
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String authenticateUser(
            String email,
            String password,
            boolean rememberMe,
            String ipAddress,
            String userAgent) {
        AppException unauthorized = AppException.unauthorized("Unauthorized.");

        User user = userRepository.findByEmail(email)
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
            throw AppException.internalServerError(
                "An unexpected error occurred while transforming the request's IP address to an InetAddress object.",
                e
            );
        }
    }

    public User createAccount(
        String firstName, 
        String lastName,
        String email, 
        String password, 
        String confirmPassword,
        boolean newsletterOptIn
    ) {
        Optional<User> existing = userRepository.findByEmail(email);


        if (!password.equals(confirmPassword)) {
            throw AppException.badRequest("Passwords do not match.");
        }

        if (existing.isPresent()) {
            throw AppException.badRequest("User with this email already exists.");
        }

        Instant now = Instant.now();
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setNewsletterOptIn(newsletterOptIn);
        user.setStatus(UserStatus.UNVERIFIED);
        user.setCreatedAt(now);
        user.setProfile(new UserProfile());

        User result = userRepository.save(user);
        return result;
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