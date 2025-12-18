package com.example.micro_conge.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DemandeCongeRHDto {


    private String employeId;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private String motif;
    private String statut; // "en attente"
}