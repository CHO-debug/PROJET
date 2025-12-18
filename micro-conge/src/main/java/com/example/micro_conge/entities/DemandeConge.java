package com.example.micro_conge.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDate;

@Document(collection = "demande_conge")
@Data
public class DemandeConge {

    @Id
    private String idDemande; // MongoDB utilise String/ObjectId

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String motif;

    // Référence à l'employé par ID
    private String employeId;
    private String idRh;


    // Référence au statut par ID (ou tu peux l'imbriquer directement)
    private String statutId;
}
