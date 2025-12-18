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

    @Operation(summary = "Mes demandes de congé (employé)")
    @GetMapping("/me/{id}")
    public ResponseEntity<List<DemandeCongeDTO>> getMyConges(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(
                demandeCongeService.getDemandeCongeByEmployeId(id)
        );
    }


    // =====================================================
    // 🧑‍💼 RH → DEMANDES DE CONGÉ EN ATTENTE
    // =====================================================

    @Operation(summary = "Demandes de congé en attente (RH)")
    @GetMapping("/rh/en-attente")
    public ResponseEntity<List<DemandeCongeRHDto>> getDemandesEnAttentePourRH() {
        List<DemandeCongeRHDto> demandes = demandeCongeService.getDemandesEnAttentePourRH();
        if (demandes.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 si aucune demande en attente
        }
        return ResponseEntity.ok(demandes); // 200 OK avec la liste
    }

    // Accepter une demande
    @PutMapping("/demande_conge/accepter/{demandeId}")
    public ResponseEntity<DemandeCongeRHDto> accepterDemande(@PathVariable String demandeId) {
        DemandeCongeRHDto dto = demandeCongeService.changerStatutPourRH(demandeId, "accepté");
        if (dto == null) {
            return ResponseEntity.notFound().build(); // 404 si demande inexistante
        }
        return ResponseEntity.ok(dto); // 200 OK avec la demande mise à jour
    }

    // Refuser une demande
    @PutMapping("/demande_conge/refuser/{demandeId}")
    public ResponseEntity<DemandeCongeRHDto> refuserDemande(@PathVariable String demandeId) {
        DemandeCongeRHDto dto = demandeCongeService.changerStatutPourRH(demandeId, "rejeté");
        if (dto == null) {
            return ResponseEntity.notFound().build(); // 404 si demande inexistante
        }
        return ResponseEntity.ok(dto); // 200 OK avec la demande mise à jour
    }


}
