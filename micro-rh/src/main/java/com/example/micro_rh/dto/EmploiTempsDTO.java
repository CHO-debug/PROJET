package com.example.micro_rh.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EmploiTempsDTO {
    private String employeId;   // ID de l'employé
    private String rhId;        // ID du RH qui planifie
    private LocalDate date;     // Date de la tâche
    private String heure;       // Heure prévue
    private String tache;       // Description de la tâche
}
