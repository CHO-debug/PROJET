package com.example.micro_conge.dto;

import lombok.Data;
import java.util.List;

@Data
public class RHDto {

    private String id;
    private String email;
    private String nom;
    private String departement;

    // Lecture seule
    private List<String> demandesCongeIds;
}