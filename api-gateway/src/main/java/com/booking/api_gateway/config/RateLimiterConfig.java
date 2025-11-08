package com.booking.api_gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Configuration
public class RateLimiterConfig {

    /**
     * This bean defines how the rate limiter identifies a user.
     * We extract the username from the authenticated Principal (provided by Keycloak).
     * This ensures that each user gets their own rate limit bucket, fulfilling the requirement.
     */
    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> exchange.getPrincipal()
                .map(Principal::getName)
                .defaultIfEmpty("anonymous"); // Fallback for unauthenticated requests
    }
}
