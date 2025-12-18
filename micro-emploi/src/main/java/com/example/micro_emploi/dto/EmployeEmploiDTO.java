package com.example.micro_emploi.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EmployeEmploiDTO {

      // ID de l'employé
    private String nom;            // Nom de l'employé (hérité de Personne si nécessaire)

    private LocalDate date;        // Date de la tâche/planning
    private String heure;          // Heure de la tâche
    private String tache;          // Description de la tâche

    // Optionnel : ID du RH qui a validé ou planifié

}
