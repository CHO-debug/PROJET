package com.example.micro_employe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmploiTempsDto {

    private String id;          // ID de l'emploi du temps
    private LocalDate date;     // Date de la tâche
    private String heure;       // Plage horaire
    private String tache;       // Titre de la tâche
    private String idRh;        // ID RH responsable
    private List<String> taches; // Liste des sous-tâches
}
