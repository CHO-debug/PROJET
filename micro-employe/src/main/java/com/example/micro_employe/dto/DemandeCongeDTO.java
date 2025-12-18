package com.example.micro_employe.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DemandeCongeDTO {
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String motif;
    private String statut; // ex : "en attente", "acceptée", "rejetée"
}
