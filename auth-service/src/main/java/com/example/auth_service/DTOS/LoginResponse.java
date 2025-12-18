package com.example.auth_service.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String id;
    private String nom;
    private String email;
    private String role; // EMPLOYE/RH
    private String token; // null pour l'instant (option JWT)
}