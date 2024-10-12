package com.orion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class OrionRentalApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrionRentalApplication.class, args);
	}

}