package com.example.micro_employe.entities; // même package qu'Employe

import org.springframework.data.annotation.Id;
import lombok.Data;

@Data
public abstract class Personne {

    @Id
    private String id; // MongoDB utilise String/ObjectId

    private String nom;
    private String email;
    private String motDePasse;

}
