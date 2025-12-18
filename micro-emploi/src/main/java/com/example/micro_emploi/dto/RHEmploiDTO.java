package com.example.micro_emploi.dto;

import java.util.List;

// DTO pour le frontend
public class RHEmploiDTO {
    private String date;
    private String heure;
    private String tache;
    private List<String> taches;
    private String employeId; // on garde employeId

    public RHEmploiDTO(String date, String heure, String tache, List<String> taches, String employeId) {
        this.date = date;
        this.heure = heure;
        this.tache = tache;
        this.taches = taches;
        this.employeId = employeId;
    }

    // getters
    public String getDate() { return date; }
    public String getHeure() { return heure; }
    public String getTache() { return tache; }
    public List<String> getTaches() { return taches; }
    public String getEmployeId() { return employeId; }
}
