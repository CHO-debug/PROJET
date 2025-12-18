package com.example.micro_rh.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DemandeCongeDTO {

    // Correspond à id_demande dans MongoDB
    private String demandeId;

    // Correspond à id dans MongoDB (employé)
    private String employeId;

    // Correspond à rhId dans MongoDB (optionnel)
    private String rhId;

    // Correspond à date_debut dans MongoDB
    private LocalDate dateDebut;

    // Correspond à date_fin dans MongoDB
    private LocalDate dateFin;

    private String motif;        // Motif du congé
    private String statut;       // "en attente", "en cours", "rejetée"

    // Si nécessaire, ajouter le texte original pour debug / parsing
    private String texteOriginal;
}
