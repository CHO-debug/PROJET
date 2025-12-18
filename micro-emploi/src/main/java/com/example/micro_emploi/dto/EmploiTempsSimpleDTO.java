package com.example.micro_emploi.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EmploiTempsSimpleDTO {
    private LocalDate date;
    private String heure;
    private String tache;
}
