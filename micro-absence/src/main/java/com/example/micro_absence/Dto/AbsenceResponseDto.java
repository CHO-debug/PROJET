package com.example.micro_absence.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbsenceResponseDto {
    private String dateInterval; // Exemple : "2025-12-15 → 2025-12-17"
    private String libelle;
    private String motif;
    private int duree;

    public String getDateInterval() { return dateInterval; }
    public void setDateInterval(String dateInterval) { this.dateInterval = dateInterval; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public int getDuree() { return duree; }
    public void setDuree(int duree) { this.duree = duree; }
}
