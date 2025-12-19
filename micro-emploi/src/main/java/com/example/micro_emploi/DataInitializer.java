package com.example.micro_emploi;

import com.example.micro_emploi.entities.EmploiTemps;
import com.example.micro_emploi.repositories.EmploiTempsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.Arrays;

@Configuration
public class DataInitializer {

    //  @Bean
    CommandLineRunner initDatabase(EmploiTempsRepository emploiTempsRepository) {
        return args -> {

            // -----------------------------
            // 1️⃣ Supprimer toutes les anciennes tâches
            // -----------------------------
            emploiTempsRepository.deleteAll();
            System.out.println("Anciennes tâches supprimées.");

            // -----------------------------
            // 2️⃣ Définir l'employé et le RH
            // -----------------------------
            String employeId = "693d747bae41e204be0d48ac";
            String rhId = "693d7410608c505a087a968e";

            // -----------------------------
            // 3️⃣ Créer les tâches pour cet employé sous ce RH
            // -----------------------------
            EmploiTemps tache1 = new EmploiTemps();
            tache1.setDate(LocalDate.now());
            tache1.setHeure("09:00 - 09:30");
            tache1.setTache("Stand-up");
            tache1.setEmployeId(employeId);
            tache1.setIdRh(rhId);
            tache1.setTaches(Arrays.asList("Point sur l’avancement", "Blocages"));

            EmploiTemps tache2 = new EmploiTemps();
            tache2.setDate(LocalDate.now());
            tache2.setHeure("09:30 - 12:30");
            tache2.setTache("Développement endpoints");
            tache2.setEmployeId(employeId);
            tache2.setIdRh(rhId);
            tache2.setTaches(Arrays.asList("Créer API REST", "Services backend"));

            EmploiTemps tache3 = new EmploiTemps();
            tache3.setDate(LocalDate.now());
            tache3.setHeure("13:30 - 15:30");
            tache3.setTache("Connexion base de données");
            tache3.setEmployeId(employeId);
            tache3.setIdRh(rhId);
            tache3.setTaches(Arrays.asList("Créer tables", "Collections", "Relations CRUD"));

            EmploiTemps tache4 = new EmploiTemps();
            tache4.setDate(LocalDate.now());
            tache4.setHeure("15:30 - 17:30");
            tache4.setTache("Validation des données");
            tache4.setEmployeId(employeId);
            tache4.setIdRh(rhId);
            tache4.setTaches(Arrays.asList("Implémenter règles métier", "Validations"));

            // -----------------------------
            // 4️⃣ Sauvegarde dans MongoDB
            // -----------------------------
            emploiTempsRepository.saveAll(Arrays.asList(tache1, tache2, tache3, tache4));

            System.out.println("Emploi du temps initialisé pour l'employé avec ID : "
                    + employeId + " sous le RH : " + rhId);
        };
    }
}
