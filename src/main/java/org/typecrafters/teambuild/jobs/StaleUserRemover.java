package org.typecrafters.teambuild.jobs;

import java.time.Duration;
import java.time.Instant;

import org.springframework.scheduling.annotation.Scheduled;
import org.typecrafters.teambuild.domain.enums.UserStatus;
import org.typecrafters.teambuild.repository.UserRepository;

public class StaleUserRemover {
    private final UserRepository userRepository;
    
    public StaleUserRemover(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 0 * * MON")
    public void removeStaleUsers() {
        Instant lastMonth = Instant.now().minus(Duration.ofDays(30));
        userRepository.deleteAllByStatusAndCreatedAtBefore(UserStatus.UNVERIFIED, lastMonth);
    }
}