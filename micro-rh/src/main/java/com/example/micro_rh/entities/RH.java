package com.example.micro_rh.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

import java.util.List;

@Document(collection = "rh")
@Data
public class RH {

    @Id
    private String id; // MongoDB utilise String/ObjectId
    private String email; // mot de passe hashé
    private String nom;        // Nouveau

    private String password; // mot de passe hashé

    private String departement;
    private List<String> emploisTempsIds; // Ids des documents EmploiTemps

    // Liste des demandes de congé que ce RH gère
    private List<String> demandesCongeIds; // Ids des documents DemandeConge

    // Liste des absences que ce RH gère
    private List<String> absencesIds; // Ids des documents Absence
}
