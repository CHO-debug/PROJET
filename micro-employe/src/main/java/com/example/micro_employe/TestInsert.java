package com.example.micro_employe;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;
import com.example.micro_employe.entities.Employe;
import com.example.micro_employe.Repositories.EmployeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Component
public class TestInsert implements CommandLineRunner {

    private final EmployeRepository employeRepository;
    private final PasswordEncoder passwordEncoder; // injecté par Spring

    public TestInsert(EmployeRepository employeRepository, PasswordEncoder passwordEncoder) {
        this.employeRepository = employeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (employeRepository.count() == 0) { // évite les doublons
            Employe e = new Employe();
            e.setNom("Chorouk");
            e.setEmail("chorouk@example.com");

            // 🔐 crypter le mot de passe
            e.setMotDePasse(passwordEncoder.encode("123456"));

            e.setPoste("Développeur");
            e.setSoldeConge(20);
            e.setAbsenceIds(List.of());
            e.setDemandeCongeIds(List.of());
            e.setEmploiTempsIds(List.of());

            employeRepository.save(e);
            System.out.println("✅ Employé inséré avec mot de passe crypté !");
        }
    }
}
