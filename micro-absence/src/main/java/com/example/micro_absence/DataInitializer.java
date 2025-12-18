package com.example.micro_absence;

import com.example.micro_absence.entities.Absence;
import com.example.micro_absence.entities.TypeAbsence;
import com.example.micro_absence.repositories.AbsenceRepository;
import com.example.micro_absence.repositories.TypeAbsenceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(TypeAbsenceRepository typeRepo,
                                   AbsenceRepository absenceRepo) {
        return args -> {

            // ---------- TYPES D’ABSENCE ----------
            TypeAbsence justifie =
                    typeRepo.save(new TypeAbsence("Justifiée", true));

            TypeAbsence nonJustifie =
                    typeRepo.save(new TypeAbsence("Non Justifiée", false));

            // ---------- ABSENCES EXISTANTES ----------
            String employeExistantId = "6932ee5daa6516cf5afdf57f";

            Absence abs1 = new Absence();
            abs1.setDate(LocalDate.of(2025, 12, 15));
            abs1.setTypeId(justifie.getIdType());
            abs1.setEmployeId(employeExistantId);
            abs1.setMotif("Maladie");
            abs1.setDuree(1);

            Absence abs2 = new Absence();
            abs2.setDate(LocalDate.of(2025, 12, 16));
            abs2.setTypeId(nonJustifie.getIdType());
            abs2.setEmployeId(employeExistantId);
            abs2.setMotif("NONE");
            abs2.setDuree(2);

            Absence abs3 = new Absence();
            abs3.setDate(LocalDate.of(2025, 12, 17));
            abs3.setTypeId(nonJustifie.getIdType());
            abs3.setEmployeId(employeExistantId);
            abs3.setMotif("NONE");
            abs3.setDuree(1);

            Absence abs4 = new Absence();
            abs4.setDate(LocalDate.of(2025, 12, 18));
            abs4.setTypeId(nonJustifie.getIdType());
            abs4.setEmployeId(employeExistantId);
            abs4.setMotif("NONE");
            abs4.setDuree(3);

            // ---------- ABSENCE POUR UN AUTRE EMPLOYÉ ----------
            String nouvelEmployeId = "693d747bae41e204be0d48ad";

            Absence abs5 = new Absence();
            abs5.setDate(LocalDate.of(2025, 12, 20));
            abs5.setTypeId(justifie.getIdType());
            abs5.setEmployeId(nouvelEmployeId);
            abs5.setMotif("Congé maladie");
            abs5.setDuree(2);

            // ---------- NOUVELLE ABSENCE POUR L’EMPLOYÉ DEMANDÉ ----------
            String employeDemandeId = "6932ee5daa6516cf5afdf57";

            Absence abs6 = new Absence();
            abs6.setDate(LocalDate.of(2025, 12, 18));
            abs6.setTypeId(justifie.getIdType());
            abs6.setEmployeId(employeDemandeId);
            abs6.setMotif("Maladie");
            abs6.setDuree(1);

            // ---------- ENREGISTRER TOUTES LES ABSENCES ----------
            absenceRepo.saveAll(List.of(abs1, abs2, abs3, abs4, abs5, abs6));

            System.out.println("✅ Initialisation terminée : toutes les absences ajoutées");
        };
    }
}
