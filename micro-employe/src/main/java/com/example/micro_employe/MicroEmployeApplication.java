package com.example.micro_employe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MicroEmployeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroEmployeApplication.class, args);
	}

}
