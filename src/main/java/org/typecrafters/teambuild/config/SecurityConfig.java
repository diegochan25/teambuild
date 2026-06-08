package org.typecrafters.teambuild.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
    private final int rounds = 10;
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(rounds);
    }
}
