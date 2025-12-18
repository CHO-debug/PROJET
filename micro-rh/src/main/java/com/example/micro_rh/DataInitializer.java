package com.example.micro_rh;

import com.example.micro_rh.entities.RH;
import com.example.micro_rh.repository.RHRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RHRepository rhRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) throws Exception {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Initialiser RH Finance
        if (!rhRepository.existsByDepartement("Finance")) {
            RH rhFinance = new RH();
            rhFinance.setNom("AYA Zouity");
            rhFinance.setEmail("ayazty@gmail.com");
            rhFinance.setDepartement("Finance");

            // 🔐 crypter mot de passe
            rhFinance.setPassword(encoder.encode("motdepasse123"));

            rhRepository.save(rhFinance);
        }

        // Initialiser RH RH
        if (!rhRepository.existsByDepartement("RH")) {
            RH rhRH = new RH();
            rhRH.setNom("Bob Martin");
            rhRH.setEmail("bob.martin@example.com");
            rhRH.setDepartement("RH");

            // 🔐 crypter mot de passe
            rhRH.setPassword(encoder.encode("admin123"));

            rhRepository.save(rhRH);
        }

        System.out.println("✅ RH initiaux ajoutés en base (mot de passe crypté) !");

        // Vérifier et créer la collection 'demande_conge'
        String collectionName = "demande_conge";
        if (!mongoTemplate.collectionExists(collectionName)) {
            mongoTemplate.createCollection(collectionName);
            System.out.println("✅ Collection 'demande_conge' créée !");
        } else {
            System.out.println("ℹ️ Collection 'demande_conge' existe déjà, pas de création.");
        }
    }
}
