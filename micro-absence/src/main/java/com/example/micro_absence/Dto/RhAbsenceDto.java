package com.example.micro_absence.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RhAbsenceDto {
    private String employeId;   // ID de l'employé concerné
    private String date;        // Date de l'absence, format yyyy-MM-dd
    private int duree;          // Durée en jours
    private String motif;       // Motif de l'absence
    private String libelle;
}
