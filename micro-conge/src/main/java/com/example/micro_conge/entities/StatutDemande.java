package com.example.micro_conge.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "statuts_demande")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatutDemande {

    @Id
    private String idStatut;

    private String libelle; // ex : "en attente", "en cours", "rejetée"
}
