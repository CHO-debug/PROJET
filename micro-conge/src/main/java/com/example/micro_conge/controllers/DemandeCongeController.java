package com.example.micro_conge.controllers;

import com.example.micro_conge.dto.DemandeCongeDTO;
import com.example.micro_conge.dto.DemandeCongeRHDto;
import com.example.micro_conge.services.DemandeCongeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Congés – Employé & RH")
@RestController
@RequestMapping("/conges")
public class DemandeCongeController {

    @Autowired
    private DemandeCongeService demandeCongeService;

    // =====================================================
    // 👤 EMPLOYÉ CONNECTÉ → MES DEMANDES DE CONGÉ
    // =====================================================

    @Operation(summary = "Mes demandes de congé (employé connecté)")
    @GetMapping("/me")
    public ResponseEntity<List<DemandeCongeDTO>> getMyConges(
            @RequestHeader("X-User-Id") String employeId
    ) {
        return ResponseEntity.ok(
                demandeCongeService.getDemandeCongeByEmployeId(employeId)
        );
    }

    // =====================================================
    // 🧑‍💼 RH → DEMANDES DE CONGÉ EN ATTENTE
    // =====================================================

    @Operation(summary = "Demandes de congé en attente (RH)")
    @GetMapping("/rh/en-attente")
    public ResponseEntity<List<DemandeCongeRHDto>> getDemandesEnAttentePourRH() {
        return ResponseEntity.ok(
                demandeCongeService.getDemandesEnAttentePourRH()
        );
    }
    // Accepter une demande
    @PutMapping("/demande_conge/accepter/{demandeId}")
    public ResponseEntity<DemandeCongeRHDto> accepterDemande(@PathVariable String demandeId) {
        DemandeCongeRHDto dto = demandeCongeService.changerStatutPourRH(demandeId, "accepté");
        return ResponseEntity.ok(dto);
    }

    // Refuser une demande
    @PutMapping("/demande_conge/refuser/{demandeId}")
    public ResponseEntity<DemandeCongeRHDto> refuserDemande(@PathVariable String demandeId) {
        DemandeCongeRHDto dto = demandeCongeService.changerStatutPourRH(demandeId, "rejeté");
        return ResponseEntity.ok(dto);
    }

}
