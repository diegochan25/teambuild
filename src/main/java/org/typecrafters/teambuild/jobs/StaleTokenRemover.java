package org.typecrafters.teambuild.jobs;
import java.time.Duration;
import java.time.Instant;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.typecrafters.teambuild.repository.TokenRepository;

@Component
public class StaleTokenRemover {
    private final TokenRepository tokenRepository;
    
    public StaleTokenRemover(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Scheduled(cron = "0 0 0 * * MON")
    public void removeStaleTokens() {
        Instant lastMonth = Instant.now().minus(Duration.ofDays(30));
        this.tokenRepository.deleteAllByExpiresAtBefore(lastMonth);
    }
}
