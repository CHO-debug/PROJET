package com.example.micro_absence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Document(collection = "types_absence")
@Data
public class TypeAbsence {
    @Id
    private String idType;
    private String libelle;
    private boolean justificationRequise;

    public TypeAbsence(String libelle, boolean justificationRequise) {
        this.libelle = libelle;
        this.justificationRequise = justificationRequise;
    }
}
