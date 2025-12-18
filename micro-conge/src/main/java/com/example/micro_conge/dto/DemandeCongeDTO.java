package com.example.micro_conge.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DemandeCongeDTO {
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String motif;
    private String statut;  // Libellé du statut
}
