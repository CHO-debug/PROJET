package com.example.micro_rh.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public abstract class Personne {

    @Id
    private String id; // MongoDB utilise String/ObjectId

    private String nom;
    private String email;
    private String motDePasse;

}