package com.booking.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                    .authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/bookings/**", "/api/inventory/**").authenticated()
                        .anyExchange().permitAll()
                    )
                    .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}))
                    .build();
    }
}
