package com.example.micro_conge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeDto {

    private String id;          // identifiant de l'employé
    private String nom;         // nom complet
    private String poste;       // poste de l'employé
    private int soldeConge;     // solde de congés restants
}
