package org.typecrafters.teambuild.jobs;

import java.time.Duration;
import java.time.Instant;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.typecrafters.teambuild.repository.SessionRepository;

@Component
public class StaleSessionRemover {
    private final SessionRepository sessionRepository;
    
    public StaleSessionRemover(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Scheduled(cron = "0 30 0 * * MON")
    public void removeStaleSessions() {
        Instant lastMonth = Instant.now().minus(Duration.ofDays(30));
        sessionRepository.deleteAllByExpiresAtBeforeOrRevokedAtBefore(lastMonth, lastMonth);
    }
}