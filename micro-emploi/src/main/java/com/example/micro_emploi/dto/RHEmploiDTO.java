package com.example.micro_emploi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// DTO pour le frontend
public class RHEmploiDTO {
    private String id ;
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

}
