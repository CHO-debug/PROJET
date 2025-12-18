package com.example.auth_service.DTOS;


import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String motDePasse;
}
