package org.typecrafters.teambuild.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.typecrafters.teambuild.domain.enums.TokenType;
import org.typecrafters.teambuild.domain.enums.UserStatus;
import org.typecrafters.teambuild.domain.exception.AppException;
import org.typecrafters.teambuild.domain.util.Crypto;
import org.typecrafters.teambuild.domain.util.TokenGenerator;
import org.typecrafters.teambuild.entity.Session;
import org.typecrafters.teambuild.entity.Token;
import org.typecrafters.teambuild.entity.User;
import org.typecrafters.teambuild.entity.UserProfile;
import org.typecrafters.teambuild.repository.SessionRepository;
import org.typecrafters.teambuild.repository.TokenRepository;
import org.typecrafters.teambuild.repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {

    private static final String FAKE_HASH = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
    private final Duration standardSession = Duration.ofDays(7);
    private final Duration extendedSession = Duration.ofDays(30);
    private final Duration emailTokenAge = Duration.ofDays(1);
    private final Duration passwordTokenAge = Duration.ofHours(1);
    private final int tokenLength = 32;
    private final int verificationCodeLength = 6;

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final TokenRepository tokenRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(PasswordEncoder passwordEncoder, MailService mailService,
        UserRepository userRepository, SessionRepository sessionRepository,
        TokenRepository verificationRepository) {
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.tokenRepository = verificationRepository;
    }

    @Transactional
    @Override
    public String authenticateUser(
        String email,
        String password,
        boolean rememberMe,
        String ipAddress,
        String userAgent
    ) {
        email = email.trim().toLowerCase();
        AppException unauthorized = AppException.unauthorized("Unauthorized.");

        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVE).orElse(null);

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

            user.setLastLoginAt(now);

            userRepository.save(user);
            Session result = sessionRepository.save(session);
            return result.getJsessionid();
        } catch (UnknownHostException e) {
            throw AppException.internalServerError(
                "An unexpected error occurred while transforming the request's IP address to an InetAddress object.",
                e
            );
        }
    }

    @Transactional
    @Override
    public User createAccount(
        String firstName,
        String lastName,
        String email,
        String password,
        String confirmPassword,
        boolean newsletterOptIn
    ) {

        email = email.trim().toLowerCase();
        Optional<User> existing = userRepository.findByEmail(email);

        if (existing.isPresent()) {
            throw AppException.badRequest("User with this email already exists.");
        }

        if (!password.equals(confirmPassword)) {
            throw AppException.badRequest("Passwords do not match.");
        }
        Instant now = Instant.now();

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setNewsletterOptIn(newsletterOptIn);
        user.setStatus(UserStatus.UNVERIFIED);
        user.setProfile(new UserProfile());

        User result = userRepository.save(user);

        String code = TokenGenerator.randomNumeric(verificationCodeLength);

        Token emailToken = new Token();
        emailToken.setCodeHash(Crypto.Hash.sha256(code));
        emailToken.setUser(result);
        emailToken.setType(TokenType.EMAIL_VERIFICATION);
        emailToken.setExpiresAt(now.plus(emailTokenAge));
        emailToken.setUsedAt(null);

        tokenRepository.save(emailToken);

        Map<String, Object> context = Map.of("firstName", firstName, "email", email, "code", code);

        mailService.sendThymeleaf(
            email,
            "Please verify your email address",
            "email/verify-email",
            context
        );

        return result;
    }

    @Transactional
    @Override
    public void revokeSession(String jsessionid) {
        Session session = sessionRepository.findByJsessionid(jsessionid).orElse(null);

        if (session != null && session.getRevokedAt() == null) {
            Instant now = Instant.now();

            session.setExpiresAt(now);
            session.setRevokedAt(now);
            sessionRepository.save(session);
        }
    }

    @Transactional
    @Override
    public void verifyEmailAddress(String email, String code) {
        String codeHash = Crypto.Hash.sha256(code);
        User user = userRepository.findByEmailAndStatus(email, UserStatus.UNVERIFIED).orElse(null);

        if (user == null) {
            return;
        }

        Token token = tokenRepository
            .findByUserAndCodeHashAndType(user, codeHash, TokenType.EMAIL_VERIFICATION)
            .orElseThrow(() -> AppException.notFound("Incorrect code."));

        if (token.getUsedAt() != null) {
            throw AppException.gone("This code expired.");
        }

        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw AppException.gone("This code expired.");
        }

        user.setStatus(UserStatus.ACTIVE);
        token.setUsedAt(Instant.now());
        userRepository.save(user);
        tokenRepository.save(token);
    }

    @Transactional
    @Override
    public void updatePassword(String email, String code, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw AppException.badRequest("Passwords do not match.");
        }

        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVE)
            .orElseThrow(() -> AppException.notFound("User not found."));

        String codeHash = Crypto.Hash.sha256(code);
        Token token = tokenRepository
            .findByUserAndCodeHashAndType(user, codeHash, TokenType.PASSWORD_RESET)
            .orElseThrow(() -> AppException.notFound("Incorrect code."));

        if (token.getUsedAt() != null) {
            throw AppException.gone("This code has already been used.");
        }

        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw AppException.gone("This code has expired.");
        }

        token.setUsedAt(Instant.now());
        tokenRepository.save(token);

        user.setPasswordHash(passwordEncoder.encode(password));
        userRepository.save(user);
        sessionRepository.deleteAllByUser(user);
    }

    @Transactional
    @Override
    public void sendPasswordResetCode(String to) {
        User user = userRepository.findByEmailAndStatus(to, UserStatus.ACTIVE).orElse(null);

        if (user == null) {
            return;
        }

        String code = TokenGenerator.randomNumeric(verificationCodeLength);
        Instant now = Instant.now();

        Token passwordToken = new Token();
        passwordToken.setCodeHash(Crypto.Hash.sha256(code));
        passwordToken.setUser(user);
        passwordToken.setType(TokenType.PASSWORD_RESET);
        passwordToken.setExpiresAt(now.plus(passwordTokenAge));
        passwordToken.setUsedAt(null);

        Map<String, Object> context = Map
            .of("firstName", user.getFirstName(), "email", user.getEmail(), "code", code);

        tokenRepository.deleteAllByUserAndType(user, TokenType.PASSWORD_RESET);
        tokenRepository.save(passwordToken);

        mailService.sendThymeleaf(
            user.getEmail(),
            "Your password reset request",
            "email/reset-password",
            context
        );
    }

    @Transactional
    @Override
    public void verifyPasswordResetCode(String email, String code) {
        String codeHash = Crypto.Hash.sha256(code);
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVE)
            .orElse(null);

        if (user == null) return;

        Token token = tokenRepository
            .findByUserAndCodeHashAndType(user, codeHash, TokenType.PASSWORD_RESET)
            .orElseThrow(() -> AppException.notFound("Incorrect code."));

        if (token.getUsedAt() != null) {
            throw AppException.gone("This code expired.");
        }

        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw AppException.gone("This code expired.");
        }
    }
}
