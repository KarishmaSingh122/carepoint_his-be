package com.suma.carepoint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CarepointApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarepointApplication.class, args);
	}

}
