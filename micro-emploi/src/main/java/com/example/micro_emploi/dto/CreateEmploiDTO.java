package com.example.micro_emploi.dto;

import java.time.LocalDate;
import java.util.List;

public class CreateEmploiDTO {
    private LocalDate date;
    private String heure;
    private String tache;
    private List<String> taches;
    private String employeId;

    // getters et setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getHeure() { return heure; }
    public void setHeure(String heure) { this.heure = heure; }

    public String getTache() { return tache; }
    public void setTache(String tache) { this.tache = tache; }

    public List<String> getTaches() { return taches; }
    public void setTaches(List<String> taches) { this.taches = taches; }

    public String getEmployeId() { return employeId; }
    public void setEmployeId(String employeId) { this.employeId = employeId; }
}
