package com.pproject.sStore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class SStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(SStoreApplication.class, args);
	}

}
