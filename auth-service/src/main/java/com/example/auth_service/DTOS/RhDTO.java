package com.example.auth_service.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RhDTO {
    private String id;
    private String nom;
    private String email;
    private String password;
}
