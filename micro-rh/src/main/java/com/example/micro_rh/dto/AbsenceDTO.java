package com.example.micro_rh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class AbsenceDTO {

    private String employeId;    // ID de l'employé
    private String dateInterval;  // ex: "2025-12-16 à 2025-12-17"
    private String libelle;       // type d'absence: "Justifiée" / "Non Justifiée"
    private String motif;         // motif de l'absence
    private int duree;            // durée en jours

    // Getters & Setters
    public String getEmployeId() { return employeId; }
    public void setEmployeId(String employeId) { this.employeId = employeId; }

    public String getDateInterval() { return dateInterval; }
    public void setDateInterval(String dateInterval) { this.dateInterval = dateInterval; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public int getDuree() { return duree; }
    public void setDuree(int duree) { this.duree = duree; }
}


