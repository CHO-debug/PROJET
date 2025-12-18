
package com.example.micro_emploi;

import com.example.micro_emploi.entities.EmploiTemps;
import com.example.micro_emploi.repositories.EmploiTempsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
@SpringBootApplication
@EnableDiscoveryClient
public class MicroEmploiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroEmploiApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(EmploiTempsRepository repository) {
        return args -> {
            EmploiTemps et = new EmploiTemps();
            et.setDate(LocalDate.now());
            et.setHeure("09:00 - 11:00");
            et.setTache("Réunion équipe");
            et.setEmployeId("12345");
            et.setIdRh("rh56789");
            repository.save(et);

            System.out.println("Document inséré avec succès !");
        };
    }
}
