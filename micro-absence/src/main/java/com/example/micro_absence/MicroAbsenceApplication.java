package com.example.micro_absence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroAbsenceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroAbsenceApplication.class, args);
	}

}
