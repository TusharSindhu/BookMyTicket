package com.booking.booking_service;

import com.booking.booking_service.entity.Customer;
import com.booking.booking_service.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingServiceApplication.class, args);
    }

    // Creates a sample customer on startup for easy testing.
    @Bean
    CommandLineRunner run(CustomerRepository customerRepository) {
        return args -> {
            if (customerRepository.count() == 0) {
                customerRepository.save(new Customer(null, "John Doe", "john.doe@example.com"));
                customerRepository.save(new Customer(null, "Sam Alt", "sam.alt@example.com"));
                customerRepository.save(new Customer(null, "Felix Kjell", "felix.kjell@example.com"));
                System.out.println("Created sample customers");
            }
        };
    }
}
