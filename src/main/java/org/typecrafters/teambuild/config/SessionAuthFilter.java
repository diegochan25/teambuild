package org.typecrafters.teambuild.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.typecrafters.teambuild.entity.Session;
import org.typecrafters.teambuild.repository.SessionRepository;

@Component
public class SessionAuthFilter extends OncePerRequestFilter {

    private final SessionRepository sessionRepository;

    public SessionAuthFilter(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        String jsessionid = extractJsessionid(request);

        if (jsessionid != null) {
            Session session = sessionRepository.findByJsessionid(jsessionid).orElse(null);

            if (isValid(session)) {
                var auth = UsernamePasswordAuthenticationToken.authenticated(
                        session.getUser(),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER")));
                SecurityContextHolder.getContext().setAuthentication(auth);
                session.setLastAccessedAt(Instant.now());
                sessionRepository.save(session);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractJsessionid(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        return Arrays.stream(cookies)
                .filter(c -> "JSESSIONID".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    private boolean isValid(Session session) {
        if (session == null) return false;
        if (session.getRevokedAt() != null) return false;
        return !session.getExpiresAt().isBefore(Instant.now());
    }
}
