package com.booking.booking_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    // Client we'll use to communicate with the Inventory Service.
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
