package com.booking.inventory_service;

import com.booking.inventory_service.service.InventoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {

        SpringApplication.run(InventoryServiceApplication.class, args);
	}

    /**
    * This CommandLineRunner bean will be executed on application startup.
    * We use it to call the service to add some sample tickets
    */
    @Bean
    CommandLineRunner run(InventoryService inventoryService) {
        return args -> {
            inventoryService.addSampleTickets();
        };
    }

}
