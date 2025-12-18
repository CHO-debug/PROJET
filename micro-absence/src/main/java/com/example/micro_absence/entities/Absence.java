package com.example.micro_absence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "absence")
public class Absence {

    @Id
    private String id;

    private LocalDate date;      // Date de l'absence
    private String typeId;       // Référence à TypeAbsence
    private String employeId;    // Référence à Employé
    private String motif;        // Motif de l’absence
    private int duree;           // Durée en jours


    // Getters / Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getTypeId() { return typeId; }
    public void setTypeId(String typeId) { this.typeId = typeId; }

    public String getEmployeId() { return employeId; }
    public void setEmployeId(String employeId) { this.employeId = employeId; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public int getDuree() { return duree; }
    public void setDuree(int duree) { this.duree = duree; }


}
