package com.example.micro_emploi.Controller;

import com.example.micro_emploi.dto.CreateEmploiDTO;
import com.example.micro_emploi.dto.EmploiTempsSimpleDTO;
import com.example.micro_emploi.dto.RHEmploiDTO;
import com.example.micro_emploi.entities.EmploiTemps;
import com.example.micro_emploi.services.EmploiTempsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Gestion des emplois du temps", description = "Endpoints pour gérer les emplois du temps")
@RestController
@RequestMapping("/emplois")
public class EmploiTempsController {

    @Autowired
    private EmploiTempsService emploiTempsService;

    @Operation(summary = "Lister les plannings de l'employé connecté")
    @GetMapping("/me")
    public ResponseEntity<List<EmploiTempsSimpleDTO>> getMyEmploi(
            @RequestHeader("X-User-Id") String employeId) {

        List<EmploiTempsSimpleDTO> emplois = emploiTempsService.getEmploiByEmployeAsSimpleDTO(employeId);
        return ResponseEntity.ok(emplois);
    }
    // ============================
    // LISTER LES EMPLOIS CRÉÉS PAR LE RH
    // ============================
    // ============================
// LISTER LES EMPLOIS CRÉÉS PAR LE RH
// ============================
    @GetMapping("/rh/me")
    public ResponseEntity<List<RHEmploiDTO>> getEmploisByRh(
            @RequestHeader("X-User-Id") String rhId) {

        List<EmploiTemps> emplois = emploiTempsService.getEmploisByRh(rhId);

        List<RHEmploiDTO> dtoList = emplois.stream()
                .map(e -> new RHEmploiDTO(
                        e.getDate().toString(),
                        e.getHeure(),
                        e.getTache(),
                        e.getTaches(),
                        e.getEmployeId() // on garde employeId tel quel
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    // ============================
// CRÉER UN EMPLOI POUR UN EMPLOYÉ
// ============================
    @PostMapping("/rh/create")
    public ResponseEntity<RHEmploiDTO> createEmploi(
            @RequestHeader("X-User-Id") String rhId,
            @RequestBody CreateEmploiDTO dto) {

        // Mapper DTO vers entité
        EmploiTemps emploiTemps = new EmploiTemps();
        emploiTemps.setDate(dto.getDate());
        emploiTemps.setHeure(dto.getHeure());
        emploiTemps.setTache(dto.getTache());
        emploiTemps.setTaches(dto.getTaches());
        emploiTemps.setEmployeId(dto.getEmployeId());
        emploiTemps.setIdRh(rhId); // Lier automatiquement le RH
        emploiTemps.setId(null);    // ID généré par MongoDB

        EmploiTemps created = emploiTempsService.createEmploi(rhId, emploiTemps);

        // Mapper vers DTO de sortie
        RHEmploiDTO response = new RHEmploiDTO(
                created.getDate().toString(),
                created.getHeure(),
                created.getTache(),
                created.getTaches(),
                created.getEmployeId()
        );

        return ResponseEntity.ok(response);
    }



@DeleteMapping("/rh/delete")
public ResponseEntity<String> deleteEmploiByTache(
     @RequestHeader("X-User-Id") String rhId,
       @RequestParam String tache) {

   boolean deleted = emploiTempsService.deleteEmploiByTache(rhId, tache);

   if (deleted) {
       return ResponseEntity.ok("Emploi supprimé avec succès");

   } else {
       return ResponseEntity.status(403).body("Impossible de supprimer cet emploi");
    }
}

}
