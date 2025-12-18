package com.example.micro_conge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
@EnableDiscoveryClient
@SpringBootApplication
// Indique à Spring où trouver tes repositories MongoDB
@EnableMongoRepositories(basePackages = "com.example.micro_conge.repositories")
public class MicroCongeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroCongeApplication.class, args);
    }
}
