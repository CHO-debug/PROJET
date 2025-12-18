package com.example.micro_rh.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  // OBLIGATOIRE pour éviter les erreurs Spring
public class RhloginReponseDTO {
    private String token;
    private String message;

    public RhloginReponseDTO(String message, String token) {
        this.message = message;
        this.token = token;
    }
}
