package com.example.micro_rh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandeCongeRHDto {
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String motif;
    private String statut;
}
