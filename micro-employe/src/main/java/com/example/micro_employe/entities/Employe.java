package com.example.micro_employe.entities;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.util.List;

@Document(collection = "employes")
@Data
public class Employe extends Personne {

    private String poste;
    private int soldeConge;

    private List<String> demandeCongeIds;
    private List<String> absenceIds;
    private List<String> emploiTempsIds;
}
