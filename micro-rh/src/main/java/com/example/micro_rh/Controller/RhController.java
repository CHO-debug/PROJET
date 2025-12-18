package com.example.micro_rh.Controller;

import com.example.micro_rh.dto.AbsenceDTO;
import com.example.micro_rh.dto.DemandeCongeRHDto;
import com.example.micro_rh.dto.EmploiTempsDTO;
import com.example.micro_rh.entities.RH;
import com.example.micro_rh.repository.RHRepository;
import com.example.micro_rh.service.RhService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rh")
@RequiredArgsConstructor
public class RhController {

    private final RHRepository rhRepository;
    private final RhService rhService;

    // =========================
    // GET RH par email
    // =========================
    @GetMapping("/by-email/{email}")
    public ResponseEntity<RH> getByEmail(@PathVariable String email) {
        return rhRepository.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    // =========================
    // Test
    // =========================
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Microservice RH fonctionne !");
    }

    @GetMapping("/{rhId}/absences")
    public ResponseEntity<List<AbsenceDTO>> getAbsences(@PathVariable String rhId) {
        // ✅ Appel sur l'instance injectée du service
        List<AbsenceDTO> absences = rhService.getAbsencesCreatedByRh(rhId);
        return ResponseEntity.ok(absences);
    }
    @PostMapping("/create-absence")
    public ResponseEntity<AbsenceDTO> createAbsence(@RequestHeader("X-User-Id") String rhId,
                                                    @RequestBody AbsenceDTO dto) {
        AbsenceDTO created = rhService.createAbsenceByRh(rhId, dto);
        return ResponseEntity.ok(created);
    }
    @GetMapping("/demandes/en-attente")
    public ResponseEntity<List<DemandeCongeRHDto>> getDemandesEnAttente() {
        List<DemandeCongeRHDto> demandes = rhService.getDemandesEnAttentePourRH();
        return ResponseEntity.ok(demandes);
    }

    @PutMapping("/demandes/accepter/{demandeId}")
    public ResponseEntity<DemandeCongeRHDto> accepterDemande(@PathVariable String demandeId) {
        return ResponseEntity.ok(rhService.accepterDemande(demandeId));
    }

    @PutMapping("/demandes/refuser/{demandeId}")
    public ResponseEntity<DemandeCongeRHDto> refuserDemande(@PathVariable String demandeId) {
        return ResponseEntity.ok(rhService.refuserDemande(demandeId));
    }

    @GetMapping("/emplois")
    public ResponseEntity<List<EmploiTempsDTO>> getEmploisByRh(@RequestHeader("X-User-Id") String rhId) {
        return ResponseEntity.ok(rhService.getEmploisByRh(rhId));
    }

    @PostMapping("/emplois")
    public ResponseEntity<EmploiTempsDTO> createEmploi(
            @RequestHeader("X-User-Id") String rhId,
            @RequestBody EmploiTempsDTO emploiTemps) {
        return ResponseEntity.ok(rhService.createEmploi(rhId, emploiTemps));
    }

    @DeleteMapping("/emplois/{emploiId}")
    public ResponseEntity<String> deleteEmploi(
            @RequestHeader("X-User-Id") String rhId,
            @PathVariable String emploiId) {

        boolean deleted = rhService.deleteEmploi(rhId, emploiId);
        if (deleted) {
            return ResponseEntity.ok("Emploi supprimé avec succès");
        } else {
            return ResponseEntity.status(403).body("Impossible de supprimer cet emploi");
        }
    }

    @GetMapping("/emplois/employe/{employeId}")
    public ResponseEntity<List<EmploiTempsDTO>> getEmploiByEmploye(@PathVariable String employeId) {
        return ResponseEntity.ok(rhService.getEmploiByEmploye(employeId));
    }

}
