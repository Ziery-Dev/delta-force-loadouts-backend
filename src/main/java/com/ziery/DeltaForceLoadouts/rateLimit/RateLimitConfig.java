package com.ziery.DeltaForceLoadouts.rateLimit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimitConfig {

    @Bean
    public SimpleRateLimiter buildCreateLimiter() {
        // máximo 3 builds a cada 30 segundos
        return new SimpleRateLimiter(3, 30_000);
    }
}
