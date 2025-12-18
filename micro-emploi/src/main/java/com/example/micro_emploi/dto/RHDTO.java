package com.example.micro_emploi.dto;

import lombok.Data;
import java.util.List;

@Data
public class RHDTO {

    private String id;                // ID du RH
    private String nom;               // Nom du RH
    private String email;             // Email du RH
    private String departement;       // Département
    private List<String> emploisTempsIds; // Liste des IDs des plannings que ce RH gère


}
