package com.example.micro_emploi.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@Document(collection = "emploitemps")
public class EmploiTemps {

    @Id
    private String id;

    private LocalDate date;
    private String heure;
    private String tache;

    private String employeId;
    private String idRh;
    private List<String> taches;
}