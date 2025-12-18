package com.example.micro_employe.Controller;

import com.example.micro_employe.Service.EmployeService;
import com.example.micro_employe.dto.AbsenceResponseDto;
import com.example.micro_employe.dto.DemandeCongeDTO;
import com.example.micro_employe.dto.EmploiTempsDto;
import com.example.micro_employe.entities.Employe;
import com.example.micro_employe.Repositories.EmployeRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employes")
@RequiredArgsConstructor
public class EmployeController {

    private final EmployeRepository employeRepository;
    private final EmployeService employeService;

    // Récupérer un employé par email
    @GetMapping("/by-email/{email}")
    public ResponseEntity<Employe> getByEmail(@PathVariable String email) {
        return employeRepository.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Récupérer un employé par ID avec vérification header (JWT flow)
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id,
            @RequestHeader(value = "user-id", required = false) String userIdHeader) {
        // Logique de sécurité : on vérifie que le header user-id correspond à l'ID
        // demandé
        if (userIdHeader == null || !userIdHeader.equals(id)) {
            return ResponseEntity.status(403).body("Accès refusé : Identifiant incorrect ou manquant.");
        }

        return employeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint pour tester si le service fonctionne
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Microservice Employe fonctionne !");
    }

    @GetMapping("/me/emploi")
    public ResponseEntity<List<EmploiTempsDto>> getMyEmploi(
            @RequestHeader("X-User-Id") String employeId
    ) {
        List<EmploiTempsDto> emplois = employeService.getMyEmploi(employeId);
        return ResponseEntity.ok(emplois);
    }
    @Operation(summary = "Mes demandes de congé (employé connecté)")
    @GetMapping("/me")
    public ResponseEntity<List<DemandeCongeDTO>> getMesConges(
            @RequestHeader("X-User-Id") String employeId
    ) {
        return ResponseEntity.ok(
                employeService.getMesDemandesConge(employeId)
        );
    }
}
