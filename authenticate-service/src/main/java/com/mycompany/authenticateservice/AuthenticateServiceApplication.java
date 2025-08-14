package com.mycompany.authenticateservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @ComponentScan("com.mycompany.authenticateservice") usually not needed if it's already the base package
public class AuthenticateServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticateServiceApplication.class, args);
	}

}
