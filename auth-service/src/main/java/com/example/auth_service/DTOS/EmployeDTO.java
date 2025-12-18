package com.example.auth_service.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeDTO {
    private String id;
    private String nom;
    private String email;
    private String motDePasse;


}

