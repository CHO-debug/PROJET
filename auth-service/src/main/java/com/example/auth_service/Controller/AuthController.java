package com.example.auth_service.Controller;

import com.example.auth_service.DTOS.LoginRequest;
import com.example.auth_service.DTOS.LoginResponse;
import com.example.auth_service.DTOS.EmployeDTO;
import com.example.auth_service.DTOS.RhDTO;
import com.example.auth_service.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ----- POST /login -----
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request,
            jakarta.servlet.http.HttpServletResponse response) {
        LoginResponse loginResponse = authService.login(request);

        // Création du cookie HttpOnly
        jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("JWT", loginResponse.getToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Mettre à true en production (HTTPS)
        cookie.setPath("/");
        cookie.setMaxAge(10 * 60 * 60); // 10 heures TODO: use constant

        response.addCookie(cookie);

        return ResponseEntity.ok(loginResponse);
    }

    // ----- GET /user/{email} -----
    @GetMapping("/user/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            // Vérifie d'abord chez les employés
            EmployeDTO employe = authService.getEmployeByEmail(email);
            if (employe != null) {
                return ResponseEntity.ok(employe);
            }

            // Sinon, chez les RH
            RhDTO rh = authService.getRhByEmail(email);
            if (rh != null) {
                return ResponseEntity.ok(rh);
            }

            // Aucun trouvé
            return ResponseEntity.status(404).body("Utilisateur non trouvé pour l'email : " + email);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la récupération : " + e.getMessage());
        }
    }
}
