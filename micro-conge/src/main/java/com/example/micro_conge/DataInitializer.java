package com.example.micro_conge;

import com.example.micro_conge.entities.DemandeConge;
import com.example.micro_conge.entities.StatutDemande;
import com.example.micro_conge.repositories.DemandeCongeRepository;
import com.example.micro_conge.repositories.StatutDemandeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final DemandeCongeRepository demandeRepo;
    private final StatutDemandeRepository statutRepo;

    public DataInitializer(DemandeCongeRepository demandeRepo,
                           StatutDemandeRepository statutRepo) {
        this.demandeRepo = demandeRepo;
        this.statutRepo = statutRepo;
    }

    @Override
    public void run(String... args) {

        // ✅ Créer le statut "en attente" si inexistant
        StatutDemande enAttente = statutRepo.findByLibelle("en attente")
                .orElseGet(() -> statutRepo.save(new StatutDemande(null, "en attente")));

        // =====================================================
        // 👤 Demande pour Chorouk
        // =====================================================
        if (demandeRepo.findByEmployeId("693d747bae41e204be0d48ac").isEmpty()) {

            DemandeConge demandeChorouk = new DemandeConge();
            demandeChorouk.setEmployeId("693d747bae41e204be0d48ac");
            demandeChorouk.setMotif("vacances");
            demandeChorouk.setDateDebut(LocalDate.now());
            demandeChorouk.setDateFin(LocalDate.now().plusDays(5));
            demandeChorouk.setStatutId(enAttente.getIdStatut());

            demandeRepo.save(demandeChorouk);
            System.out.println("✅ Demande ajoutée pour Chorouk");
        }

        // =====================================================
        // 👤 Demande pour un autre employé
        // =====================================================
        if (demandeRepo.findByEmployeId("a1b2c3d4e5f6g7h8i9j0").isEmpty()) {

            DemandeConge demandeAutre = new DemandeConge();
            demandeAutre.setEmployeId("a1b2c3d4e5f6g7h8i9j0");
            demandeAutre.setMotif("formation");
            demandeAutre.setDateDebut(LocalDate.now().plusDays(2));
            demandeAutre.setDateFin(LocalDate.now().plusDays(4));
            demandeAutre.setStatutId(enAttente.getIdStatut());

            demandeRepo.save(demandeAutre);
            System.out.println("✅ Demande ajoutée pour le deuxième employé");
        }
    }
}
